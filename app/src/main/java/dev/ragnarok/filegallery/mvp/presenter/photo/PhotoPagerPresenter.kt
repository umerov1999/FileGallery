package dev.ragnarok.filegallery.mvp.presenter.photo

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.core.net.toFile
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.model.Photo
import dev.ragnarok.filegallery.mvp.presenter.base.RxSupportPresenter
import dev.ragnarok.filegallery.mvp.view.IPhotoPagerView
import dev.ragnarok.filegallery.settings.Settings.get
import dev.ragnarok.filegallery.util.AssertUtils
import dev.ragnarok.filegallery.util.DownloadWorkUtils.doDownloadPhoto
import dev.ragnarok.filegallery.util.Utils
import java.io.File
import java.util.*

open class PhotoPagerPresenter internal constructor(
    initialData: ArrayList<Photo>,
    private val context: Context,
    savedInstanceState: Bundle?
) : RxSupportPresenter<IPhotoPagerView>(savedInstanceState) {
    protected var mPhotos: ArrayList<Photo> = initialData
    protected var currentIndex = 0
    private var mLoadingNow = false
    private var mFullScreen = false
    open fun close() {
        view?.closeOnly()
    }

    fun changeLoadingNowState(loading: Boolean) {
        mLoadingNow = loading
        resolveLoadingView()
    }

    private fun resolveLoadingView() {
        view?.displayPhotoListLoading(mLoadingNow)
    }

    fun refreshPagerView() {
        view?.displayPhotos(
            mPhotos,
            currentIndex
        )
    }

    override fun onGuiCreated(viewHost: IPhotoPagerView) {
        super.onGuiCreated(viewHost)
        view?.displayPhotos(
            mPhotos,
            currentIndex
        )
        refreshInfoViews()
        resolveToolbarVisibility()
        resolveButtonsBarVisible()
        resolveLoadingView()
    }

    fun firePageSelected(position: Int) {
        val old = currentIndex
        changePageTo(position)
        afterPageChangedFromUi(old, position)
    }

    protected open fun afterPageChangedFromUi(oldPage: Int, newPage: Int) {}
    private fun changePageTo(position: Int) {
        if (currentIndex == position) return
        currentIndex = position
        onPositionChanged()
    }

    fun count(): Int {
        return mPhotos.size
    }

    private fun resolveToolbarTitleSubtitleView() {
        if (!hasPhotos()) return
        val title = context.getString(R.string.image_number, currentIndex + 1, count())
        view?.setToolbarTitle(title)
        view?.setToolbarSubtitle(current.text)
    }

    private val current: Photo
        get() = mPhotos[currentIndex]

    private fun onPositionChanged() {
        refreshInfoViews()
    }

    fun refreshInfoViews() {
        resolveToolbarTitleSubtitleView()
    }

    fun fireSaveOnDriveClick() {
        val dir = File(get().main().getPhotoDir())
        if (!dir.isDirectory) {
            val created = dir.mkdirs()
            if (!created) {
                view?.showError("Can't create directory $dir")
                return
            }
        } else dir.setLastModified(Calendar.getInstance().time.time)
        val photo = current
        var path = photo.text
        val ndx = path.indexOf('/')
        if (ndx != -1) {
            path = path.substring(0, ndx)
        }
        DownloadResult(path, dir, photo)
    }

    private fun DownloadResult(Prefix: String?, diru: File, photo: Photo) {
        var dir = diru
        if (Prefix != null && get().main().isPhoto_to_user_dir()) {
            val dir_final = File(dir.absolutePath + "/" + Prefix)
            if (!dir_final.isDirectory) {
                val created = dir_final.mkdirs()
                if (!created) {
                    view?.showError("Can't create directory $dir_final")
                    return
                }
            } else dir_final.setLastModified(Calendar.getInstance().time.time)
            dir = dir_final
        }
        doDownloadPhoto(
            context,
            photo.photo_url,
            dir.absolutePath,
            (if (Prefix != null) Prefix + "_" else "") + photo.ownerId + "_" + photo.id
        )
    }

    private fun hasPhotos(): Boolean {
        return !Utils.safeIsEmpty(mPhotos)
    }

    fun firePhotoTap() {
        if (!hasPhotos()) return
        mFullScreen = !mFullScreen
        resolveToolbarVisibility()
        resolveButtonsBarVisible()
    }

    fun resolveButtonsBarVisible() {
        view?.setButtonsBarVisible(hasPhotos() && !mFullScreen)
    }

    fun resolveToolbarVisibility() {
        view?.setToolbarVisible(hasPhotos() && !mFullScreen)
    }

    val currentFile: String
        get() = Uri.parse(mPhotos[currentIndex].photo_url).toFile().absolutePath

    init {
        AssertUtils.requireNonNull(mPhotos, "'mPhotos' not initialized")
    }
}