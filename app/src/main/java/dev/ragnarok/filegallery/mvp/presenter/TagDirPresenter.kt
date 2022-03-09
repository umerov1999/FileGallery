package dev.ragnarok.filegallery.mvp.presenter

import android.os.Bundle
import dev.ragnarok.filegallery.Extensions.Companion.fromIOToMain
import dev.ragnarok.filegallery.Includes
import dev.ragnarok.filegallery.db.interfaces.ISearchRequestHelperStorage
import dev.ragnarok.filegallery.model.Audio
import dev.ragnarok.filegallery.model.FileType
import dev.ragnarok.filegallery.model.Photo
import dev.ragnarok.filegallery.model.Video
import dev.ragnarok.filegallery.model.tags.TagDir
import dev.ragnarok.filegallery.module.parcel.ParcelFlags
import dev.ragnarok.filegallery.module.parcel.ParcelNative
import dev.ragnarok.filegallery.mvp.presenter.base.RxSupportPresenter
import dev.ragnarok.filegallery.mvp.view.ITagDirView
import dev.ragnarok.filegallery.util.Objects
import dev.ragnarok.filegallery.util.Utils
import java.util.*

class TagDirPresenter(private val owner_id: Int, savedInstanceState: Bundle?) :
    RxSupportPresenter<ITagDirView>(savedInstanceState) {
    private val tagDirData: ArrayList<TagDir> = ArrayList()
    private val tagDirDataSearch: ArrayList<TagDir> = ArrayList()
    private var q: String? = null
    private val storage: ISearchRequestHelperStorage =
        Includes.stores.searchQueriesStore()

    override fun onGuiCreated(viewHost: ITagDirView) {
        super.onGuiCreated(viewHost)
        viewHost.displayData(if (q == null) tagDirData else tagDirDataSearch)
    }

    fun doSearch(str: String?) {
        val query = str?.trim { it <= ' ' }
        if (Objects.safeEquals(query, this.q)) {
            return
        }
        q = if (Utils.isEmpty(query)) {
            null
        } else {
            query
        }
        if (q == null) {
            tagDirDataSearch.clear()
            view?.displayData(tagDirData)
        } else {
            tagDirDataSearch.clear()
            for (i in tagDirData) {
                if (Utils.isEmpty(i.name)) {
                    continue
                }
                if (i.name.lowercase(Locale.getDefault())
                        .contains(q?.lowercase(Locale.getDefault()).toString())
                ) {
                    tagDirDataSearch.add(i)
                }
            }
            view?.displayData(tagDirDataSearch)
        }
    }

    private fun loadActualData() {
        appendDisposable(
            storage.getTagDirs(owner_id)
                .fromIOToMain()
                .subscribe({ onActualDataReceived(it) },
                    { t: Throwable -> onActualDataGetError(t) })
        )
    }

    fun deleteTagDir(pos: Int, owner: TagDir) {
        appendDisposable(
            storage.deleteTagDir(owner.id)
                .fromIOToMain()
                .subscribe(
                    {
                        tagDirData.removeAt(pos)
                        view?.notifyRemove(pos)
                    }, { t: Throwable -> onActualDataGetError(t) })
        )
    }

    fun scrollTo(item: String): Boolean {
        var ret = false
        val list = tagDirData
        for (i in list.indices) {
            if (!ret && list[i].path == item) {
                list[i].isSelected = true
                view?.notifyItemChanged(i)
                view?.onScrollTo(i)
                ret = true
            } else {
                if (list[i].isSelected) {
                    list[i].isSelected = false
                    view?.notifyItemChanged(i)
                }
            }
        }
        return ret
    }

    fun onClickFile(item: TagDir) {
        if (item.type == FileType.photo) {
            val list = tagDirData
            var index = 0
            var o = 0
            val mem = ParcelNative.create(ParcelFlags.NULL_LIST)
            for (i in list) {
                if (i.type != FileType.photo && i.type != FileType.video) {
                    continue
                }
                if (i.path == item.path) {
                    index = o
                }
                val photo = Photo()
                photo.id = i.fileNameHash
                photo.ownerId = i.filePathHash
                photo.photo_url = "file://" + i.path
                photo.preview_url = "thumb_file://" + i.path
                photo.isGif =
                    i.type == FileType.video || i.name.toString().endsWith("gif", true)
                photo.text = i.name
                mem.writeParcelable(photo)
                o++
            }
            mem.writeFirstInt(o)
            view?.displayGalleryUnSafe(mem.nativePointer, index, false)
        } else if (item.type == FileType.video) {
            val v = Video()
            v.id = item.fileNameHash
            v.ownerId = item.filePathHash
            v.title = item.name
            v.link = "file://" + item.path
            view?.displayVideo(v)
        } else if (item.type == FileType.audio) {
            val list = tagDirData
            var index = 0
            var o = 0
            val mAudios: ArrayList<Audio> = ArrayList()
            for (i in list) {
                if (i.type != FileType.audio) {
                    continue
                }
                if (i.path == item.path) {
                    index = o
                }
                val audio = Audio()
                audio.id = i.fileNameHash
                audio.ownerId = i.filePathHash
                audio.url = "file://" + i.path
                audio.thumb_image = "thumb_file://" + i.path

                var TrackName: String =
                    i.name
                        .replace(".mp3", "")
                val Artist: String
                val arr = TrackName.split(" - ").toTypedArray()
                if (arr.size > 1) {
                    Artist = arr[0]
                    TrackName = TrackName.replace("$Artist - ", "")
                } else {
                    Artist = i.name
                }
                audio.setIsLocal()
                audio.artist = Artist
                audio.title = TrackName

                mAudios.add(audio)
                o++
            }
            view?.startPlayAudios(mAudios, index)
        }
    }

    private fun onActualDataGetError(t: Throwable) {
        view?.showError(t)
    }

    private fun onActualDataReceived(data: List<TagDir>) {
        tagDirData.clear()
        tagDirData.addAll(data)
        view?.notifyChanges()
    }

    init {
        loadActualData()
    }
}
