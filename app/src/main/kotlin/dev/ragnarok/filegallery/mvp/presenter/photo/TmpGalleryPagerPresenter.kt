package dev.ragnarok.filegallery.mvp.presenter.photo

import android.content.Context
import android.os.Bundle
import dev.ragnarok.filegallery.model.Photo
import dev.ragnarok.filegallery.module.parcel.ParcelNative

class TmpGalleryPagerPresenter(
    source: Long, index: Int, context: Context?,
    savedInstanceState: Bundle?
) : PhotoPagerPresenter(ArrayList(0), context!!, savedInstanceState) {
    override fun close() {
        view?.returnFileInfo(currentFile)
    }

    private fun onInitialLoadingFinished(photos: List<Photo>) {
        changeLoadingNowState(false)
        mPhotos.addAll(photos)
        refreshPagerView()
        resolveButtonsBarVisible()
        resolveToolbarVisibility()
        refreshInfoViews()
    }

    init {
        currentIndex = index
        changeLoadingNowState(true)
        onInitialLoadingFinished(
            ParcelNative.fromNative(source).readParcelableList(Photo.NativeCreator)!!
        )
    }
}