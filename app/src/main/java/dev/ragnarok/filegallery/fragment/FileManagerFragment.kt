package dev.ragnarok.filegallery.fragment

import android.animation.Animator
import android.animation.ObjectAnimator
import android.app.Activity
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toFile
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import dev.ragnarok.filegallery.Constants
import dev.ragnarok.filegallery.Extra
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.activity.ActivityFeatures
import dev.ragnarok.filegallery.adapter.FileManagerAdapter
import dev.ragnarok.filegallery.adapter.FileManagerAdapter.ClickListener
import dev.ragnarok.filegallery.fragment.base.BaseMvpFragment
import dev.ragnarok.filegallery.fromIOToMain
import dev.ragnarok.filegallery.listener.*
import dev.ragnarok.filegallery.media.music.MusicPlaybackController
import dev.ragnarok.filegallery.media.music.MusicPlaybackService
import dev.ragnarok.filegallery.model.*
import dev.ragnarok.filegallery.mvp.core.IPresenterFactory
import dev.ragnarok.filegallery.mvp.core.PresenterAction
import dev.ragnarok.filegallery.mvp.presenter.FileManagerPresenter
import dev.ragnarok.filegallery.mvp.view.IFileManagerView
import dev.ragnarok.filegallery.place.PlaceFactory
import dev.ragnarok.filegallery.place.PlaceFactory.getPhotoLocalPlace
import dev.ragnarok.filegallery.place.PlaceFactory.getPlayerPlace
import dev.ragnarok.filegallery.settings.CurrentTheme
import dev.ragnarok.filegallery.settings.Settings
import dev.ragnarok.filegallery.util.CustomToast.Companion.CreateCustomToast
import dev.ragnarok.filegallery.util.RxUtils
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.util.ViewUtils
import dev.ragnarok.filegallery.view.MySearchView
import dev.ragnarok.filegallery.view.natives.rlottie.RLottieImageView
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.disposables.Disposable
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit

class FileManagerFragment : BaseMvpFragment<FileManagerPresenter, IFileManagerView>(),
    IFileManagerView, ClickListener, BackPressCallback, CanBackPressedCallback {
    // Stores names of traversed directories
    private var mRecyclerView: RecyclerView? = null
    private var mLayoutManager: StaggeredGridLayoutManager? = null
    private var empty: TextView? = null
    private var loading: RLottieImageView? = null
    private var tvCurrentDir: TextView? = null
    private var mAdapter: FileManagerAdapter? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    private var mSelected: FloatingActionButton? = null

    private var animationDispose = Disposable.disposed()
    private var mAnimationLoaded = false

    private val requestPhotoUpdate = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null && (result.data
                ?: return@registerForActivityResult)
                .extras != null
        ) {
            postPresenterReceive(object : PresenterAction<FileManagerPresenter, IFileManagerView> {
                override fun call(presenter: FileManagerPresenter) {
                    presenter.scrollTo(
                        ((result.data ?: return).extras ?: return).getString(Extra.PATH) ?: return
                    )
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        animationDispose.dispose()
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is OnSectionResumeCallback) {
            (requireActivity() as OnSectionResumeCallback).onSectionResume(SectionItem.FILE_MANAGER)
        }
        ActivityFeatures.Builder()
            .begin()
            .setBarsColored(requireActivity(), true)
            .build()
            .apply(requireActivity())
    }

    override fun getPresenterFactory(saveInstanceState: Bundle?): IPresenterFactory<FileManagerPresenter> =
        object : IPresenterFactory<FileManagerPresenter> {
            override fun create(): FileManagerPresenter {
                return FileManagerPresenter(
                    File(requireArguments().getString(Extra.PATH)!!),
                    requireArguments().getBoolean(Extra.POSITION),
                    saveInstanceState
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            TagOwnerBottomSheetSelected.SELECTED_OWNER_KEY, this
        ) { _: String?, result: Bundle ->
            presenter?.setSelectedOwner(
                result.getParcelable(Extra.NAME) ?: return@setFragmentResultListener
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_file_explorer, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.findViewById(R.id.toolbar))
        mRecyclerView = root.findViewById(R.id.list)
        empty = root.findViewById(R.id.empty)
        val mySearchView: MySearchView = root.findViewById(R.id.searchview)
        mySearchView.setRightButtonVisibility(true)
        mySearchView.setRightIcon(R.drawable.star)
        mySearchView.setLeftIcon(R.drawable.magnify)
        mySearchView.setOnBackButtonClickListener(object : MySearchView.OnBackButtonClickListener {
            override fun onBackButtonClick() {
                presenter?.doSearch(mySearchView.text.toString(), true)
            }
        })

        mySearchView.setOnAdditionalButtonClickListener(object :
            MySearchView.OnAdditionalButtonClickListener {
            override fun onAdditionalButtonClick() {
                TagOwnerBottomSheetSelected().show(parentFragmentManager, "selectOwner")
            }
        })

        mySearchView.setOnQueryTextListener(object : MySearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter?.doSearch(query, true)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter?.doSearch(newText, false)
                return false
            }
        })
        val columns = resources.getInteger(R.integer.files_column_count)
        mLayoutManager = StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL)
        mRecyclerView?.layoutManager = mLayoutManager
        mRecyclerView?.addOnScrollListener(PicassoPauseOnScrollListener(Constants.PICASSO_TAG))
        tvCurrentDir = root.findViewById(R.id.current_path)
        loading = root.findViewById(R.id.loading)

        mSwipeRefreshLayout = root.findViewById(R.id.swipeRefreshLayout)
        mSwipeRefreshLayout?.setOnRefreshListener {
            mSwipeRefreshLayout?.isRefreshing = false
            if (presenter?.canRefresh() == true) {
                mLayoutManager?.onSaveInstanceState()?.let { presenter?.backupDirectoryScroll(it) }
                presenter?.loadFiles(back = false, caches = false)
            }
        }
        ViewUtils.setupSwipeRefreshLayoutWithCurrentTheme(requireActivity(), mSwipeRefreshLayout)
        mAdapter = FileManagerAdapter(requireActivity(), Collections.emptyList())
        mAdapter?.setClickListener(this)
        mRecyclerView?.adapter = mAdapter

        val Goto: FloatingActionButton = root.findViewById(R.id.music_button)
        mSelected = root.findViewById(R.id.selected_button)
        mSelected?.setOnClickListener {
            presenter?.setSelectedOwner(null)
        }

        Goto.setOnLongClickListener {
            val curr = MusicPlaybackController.currentAudio
            if (curr != null) {
                getPlayerPlace().tryOpenWith(requireActivity())
            } else CreateCustomToast(requireActivity())
                .showToastError(R.string.null_audio)
            false
        }
        Goto.setOnClickListener {
            val curr = MusicPlaybackController.currentAudio
            if (curr != null && curr.isLocal) {
                if (presenter?.scrollTo(Uri.parse(curr.url).toFile().absolutePath) != true) {
                    CreateCustomToast(requireActivity())
                        .showToastError(R.string.audio_not_found)
                }
            } else CreateCustomToast(requireActivity())
                .showToastError(R.string.null_audio)
        }
        return root
    }

    override fun onClick(position: Int, item: FileItem) {
        if (item.type == FileType.folder) {
            val sel = File(item.file_path)
            if (presenter?.canRefresh() == true) {
                mLayoutManager?.onSaveInstanceState()?.let { presenter?.backupDirectoryScroll(it) }
                presenter?.setCurrent(sel)
            } else {
                PlaceFactory.getFileManagerPlace(sel.absolutePath, true)
                    .tryOpenWith(requireActivity())
            }
            return
        } else {
            presenter?.onClickFile(item)
        }
    }

    override fun onFixDir(item: FileItem) {
        presenter?.fireFixDirTime(item.file_path)
    }

    override fun onUpdateTimeFile(item: FileItem) {
        val tmp = File(item.file_path)
        if (tmp.setLastModified(Calendar.getInstance().time.time)) {
            showMessage(R.string.success)
            presenter?.loadFiles(back = false, caches = false)
        }
    }

    override fun onDirTag(item: FileItem) {
        TagOwnerBottomSheet.create(item)
            .show(parentFragmentManager, "tag_add")
    }

    override fun onBackPressed(): Boolean {
        if (presenter?.canLoadUp() == true) {
            mLayoutManager?.onSaveInstanceState()?.let { presenter?.backupDirectoryScroll(it) }
            presenter?.loadUp()
            return false
        }
        return true
    }

    companion object {
        fun buildArgs(path: String, base: Boolean): Bundle {
            val args = Bundle()
            args.putString(Extra.PATH, path)
            args.putBoolean(Extra.POSITION, base)
            return args
        }

        fun newInstance(args: Bundle): FileManagerFragment {
            val fragment = FileManagerFragment()
            fragment.arguments = args
            return fragment
        }

        fun isExtension(str: String, ext: Set<String>): Boolean {
            var ret = false
            for (i in ext) {
                if (str.endsWith(i, true)) {
                    ret = true
                    break
                }
            }
            return ret
        }
    }

    override fun displayData(items: ArrayList<FileItem>) {
        mAdapter?.setItems(items)
    }

    override fun resolveEmptyText(visible: Boolean) {
        empty?.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun resolveLoading(visible: Boolean) {
        animationDispose.dispose()
        if (mAnimationLoaded && !visible) {
            mAnimationLoaded = false
            val k = ObjectAnimator.ofFloat(loading, View.ALPHA, 0.0f).setDuration(1000)
            k.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                }

                override fun onAnimationEnd(animation: Animator?) {
                    loading?.clearAnimationDrawable()
                    loading?.visibility = View.GONE
                    loading?.alpha = 1f
                }

                override fun onAnimationCancel(animation: Animator?) {
                    loading?.clearAnimationDrawable()
                    loading?.visibility = View.GONE
                    loading?.alpha = 1f
                }

                override fun onAnimationRepeat(animation: Animator?) {
                }
            })
            k.start()
        } else if (mAnimationLoaded && !visible) {
            mAnimationLoaded = false
            loading?.clearAnimationDrawable()
            loading?.visibility = View.GONE
        } else if (visible) {
            animationDispose = Completable.create {
                it.onComplete()
            }.delay(300, TimeUnit.MILLISECONDS).fromIOToMain().subscribe({
                mAnimationLoaded = true
                loading?.visibility = View.VISIBLE
                loading?.fromRes(
                    R.raw.s_loading,
                    Utils.dp(180f),
                    Utils.dp(180f),
                    intArrayOf(
                        0x333333,
                        CurrentTheme.getColorPrimary(requireActivity()),
                        0x777777,
                        CurrentTheme.getColorSecondary(requireActivity())
                    )
                )
                loading?.playAnimation()
            }, RxUtils.ignore())
        }
    }

    override fun onError(throwable: Throwable) {
        mRecyclerView?.let {
            Utils.ColoredSnack(it, throwable.stackTraceToString(), Snackbar.LENGTH_LONG, Color.RED)
                .show()
        }
    }

    override fun showMessage(@StringRes res: Int) {
        mRecyclerView?.let {
            Utils.ThemedSnack(it, res, Snackbar.LENGTH_LONG)
                .show()
        }
    }

    override fun updateSelectedMode(show: Boolean) {
        mSelected?.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun notifyAllChanged() {
        mAdapter?.notifyDataSetChanged()
    }

    override fun updatePathString(file: String) {
        tvCurrentDir?.text = file
        if (requireActivity() is UpdatableNavigation) {
            (requireActivity() as UpdatableNavigation).onUpdateNavigation()
        }
    }

    override fun restoreScroll(scroll: Parcelable) {
        mLayoutManager?.onRestoreInstanceState(scroll)
    }

    override fun displayGalleryUnSafe(parcelNativePointer: Long, position: Int, reversed: Boolean) {
        getPhotoLocalPlace(parcelNativePointer, position, reversed).setActivityResultLauncher(
            requestPhotoUpdate
        ).tryOpenWith(requireActivity())
    }

    override fun displayVideo(video: Video) {
        PlaceFactory.getInternalPlayerPlace(video).tryOpenWith(requireActivity())
    }

    override fun startPlayAudios(audios: ArrayList<Audio>, position: Int) {
        MusicPlaybackService.startForPlayList(requireActivity(), audios, position, false)
        if (!Settings.get().main().isShow_mini_player())
            getPlayerPlace().tryOpenWith(requireActivity())
    }

    override fun onScrollTo(pos: Int) {
        mLayoutManager?.scrollToPosition(pos)
    }

    override fun notifyItemChanged(pos: Int) {
        mAdapter?.notifyItemChanged(pos)
    }

    override fun canBackPressed(): Boolean {
        return presenter?.canLoadUp() == true
    }
}