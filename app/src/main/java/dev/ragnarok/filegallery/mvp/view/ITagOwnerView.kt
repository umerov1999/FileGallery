package dev.ragnarok.filegallery.mvp.view

import dev.ragnarok.filegallery.model.FileItem
import dev.ragnarok.filegallery.model.tags.TagOwner
import dev.ragnarok.filegallery.mvp.core.IMvpView

interface ITagOwnerView : IMvpView {
    fun displayData(data: List<TagOwner>)
    fun notifyChanges()
    fun showError(error: Throwable)
    fun notifyAdd(index: Int)
    fun notifyRemove(index: Int)
    fun close(owner: TagOwner, item: FileItem)
}