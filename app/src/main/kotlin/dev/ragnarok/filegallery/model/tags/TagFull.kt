package dev.ragnarok.filegallery.model.tags

import android.os.Parcel
import android.os.Parcelable
import dev.ragnarok.filegallery.model.FileType

class TagFull : Parcelable {
    var name: String? = null
        private set
    var dirs: List<TagDirEntry>? = null

    constructor()
    constructor(`in`: Parcel) {
        name = `in`.readString()
        dirs = `in`.createTypedArrayList(TagDirEntry.CREATOR)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeTypedList(dirs)
    }

    fun setDirs(dirs: List<TagDirEntry>?): TagFull {
        this.dirs = dirs
        return this
    }

    fun setName(name: String?): TagFull {
        this.name = name
        return this
    }

    class TagDirEntry : Parcelable {
        var name: String? = null
            private set
        var path: String? = null
            private set

        @get:FileType
        @FileType
        var type = FileType.folder
            private set

        constructor()
        constructor(`in`: Parcel) {
            name = `in`.readString()
            path = `in`.readString()
            type = `in`.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(name)
            parcel.writeString(path)
            parcel.writeInt(type)
        }

        fun setType(@FileType type: Int): TagDirEntry {
            this.type = type
            return this
        }

        fun setName(name: String?): TagDirEntry {
            this.name = name
            return this
        }

        fun setPath(path: String?): TagDirEntry {
            this.path = path
            return this
        }

        companion object CREATOR : Parcelable.Creator<TagDirEntry> {
            override fun createFromParcel(parcel: Parcel): TagDirEntry {
                return TagDirEntry(parcel)
            }

            override fun newArray(size: Int): Array<TagDirEntry?> {
                return arrayOfNulls(size)
            }
        }
    }

    companion object CREATOR : Parcelable.Creator<TagFull> {
        override fun createFromParcel(parcel: Parcel): TagFull {
            return TagFull(parcel)
        }

        override fun newArray(size: Int): Array<TagFull?> {
            return arrayOfNulls(size)
        }
    }
}