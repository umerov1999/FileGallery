package dev.ragnarok.filegallery.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import dev.ragnarok.filegallery.Extra
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.activity.ActivityFeatures
import dev.ragnarok.filegallery.activity.ActivityUtils.supportToolbarFor
import dev.ragnarok.filegallery.adapter.TagDirAdapter
import dev.ragnarok.filegallery.listener.OnSectionResumeCallback
import dev.ragnarok.filegallery.media.music.MusicPlaybackService
import dev.ragnarok.filegallery.model.Audio
import dev.ragnarok.filegallery.model.FileType
import dev.ragnarok.filegallery.model.SectionItem
import dev.ragnarok.filegallery.model.Video
import dev.ragnarok.filegallery.model.tags.TagDir
import dev.ragnarok.filegallery.mvp.compat.AbsMvpFragment
import dev.ragnarok.filegallery.mvp.core.IPresenterFactory
import dev.ragnarok.filegallery.mvp.presenter.TagDirPresenter
import dev.ragnarok.filegallery.mvp.view.ITagDirView
import dev.ragnarok.filegallery.place.PlaceFactory
import dev.ragnarok.filegallery.settings.Settings
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.view.MySearchView


class TagDirFragment : AbsMvpFragment<TagDirPresenter, ITagDirView>(), ITagDirView,
    TagDirAdapter.ClickListener {
    private var mAdapter: TagDirAdapter? = null
    private var mLayoutManager: StaggeredGridLayoutManager? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_tag_dirs, container, false)
        (requireActivity() as AppCompatActivity).setSupportActionBar(root.findViewById(R.id.toolbar))
        val recyclerView: RecyclerView = root.findViewById(R.id.recycler_view)
        val columns = resources.getInteger(R.integer.files_column_count)
        mLayoutManager = StaggeredGridLayoutManager(columns, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = mLayoutManager
        mAdapter = TagDirAdapter(requireActivity(), emptyList())
        mAdapter?.setClickListener(this)
        recyclerView.adapter = mAdapter

        val mySearchView: MySearchView = root.findViewById(R.id.searchview)
        mySearchView.setRightButtonVisibility(false)
        mySearchView.setLeftIcon(R.drawable.magnify)
        mySearchView.setOnBackButtonClickListener(object : MySearchView.OnBackButtonClickListener {
            override fun onBackButtonClick() {
                presenter?.doSearch(mySearchView.text.toString())
            }
        })
        mySearchView.setOnQueryTextListener(object : MySearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter?.doSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                presenter?.doSearch(newText)
                return false
            }
        })
        return root
    }

    private val requestPhotoUpdate = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null && result.data!!
                .extras != null
        ) {
            presenter?.scrollTo(result.data!!.extras!!.getString(Extra.PATH)!!)
        }
    }

    override fun onResume() {
        super.onResume()
        val actionBar = supportToolbarFor(this)
        actionBar?.setTitle(R.string.tag_owner_title)
        actionBar?.subtitle = null
        if (requireActivity() is OnSectionResumeCallback) {
            (requireActivity() as OnSectionResumeCallback).onSectionResume(SectionItem.TAGS)
        }
        ActivityFeatures.Builder()
            .begin()
            .setBarsColored(requireActivity(), true)
            .build()
            .apply(requireActivity())
    }

    override fun getPresenterFactory(saveInstanceState: Bundle?): IPresenterFactory<TagDirPresenter> =
        object : IPresenterFactory<TagDirPresenter> {
            override fun create(): TagDirPresenter {
                return TagDirPresenter(requireArguments().getInt(Extra.OWNER_ID), saveInstanceState)
            }
        }

    override fun displayData(data: List<TagDir>) {
        mAdapter?.setItems(data)
    }

    override fun notifyChanges() {
        mAdapter?.notifyDataSetChanged()
    }

    override fun showError(error: Throwable) {
        Utils.showErrorInAdapter(requireActivity(), error)
    }

    override fun notifyRemove(index: Int) {
        mAdapter?.notifyItemRemoved(index)
    }

    override fun onScrollTo(pos: Int) {
        mLayoutManager?.scrollToPosition(pos)
    }

    override fun notifyItemChanged(pos: Int) {
        mAdapter?.notifyItemChanged(pos)
    }

    override fun displayGalleryUnSafe(parcelNativePointer: Long, position: Int, reversed: Boolean) {
        PlaceFactory.getPhotoLocalPlace(parcelNativePointer, position, reversed)
            .setActivityResultLauncher(
                requestPhotoUpdate
            ).tryOpenWith(requireActivity())
    }

    override fun displayVideo(video: Video) {
        PlaceFactory.getInternalPlayerPlace(video).tryOpenWith(requireActivity())
    }

    override fun startPlayAudios(audios: ArrayList<Audio>, position: Int) {
        MusicPlaybackService.startForPlayList(requireActivity(), audios, position, false)
        if (!Settings.get().main().isShow_mini_player())
            PlaceFactory.getPlayerPlace().tryOpenWith(requireActivity())
    }

    override fun onClick(position: Int, item: TagDir) {
        if (item.type == FileType.folder) {
            PlaceFactory.getFileManagerPlace(item.path, true).tryOpenWith(requireActivity())
        } else {
            presenter?.onClickFile(item)
        }
    }

    override fun onRemove(position: Int, item: TagDir) {
        presenter?.deleteTagDir(position, item)
    }

    companion object {
        fun buildArgs(ownerId: Int): Bundle {
            val args = Bundle()
            args.putInt(Extra.OWNER_ID, ownerId)
            return args
        }

        fun newInstance(args: Bundle): TagDirFragment {
            val fragment = TagDirFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
