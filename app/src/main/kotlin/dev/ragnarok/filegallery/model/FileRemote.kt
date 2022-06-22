package dev.ragnarok.filegallery.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import dev.ragnarok.filegallery.module.parcel.ParcelNative

@Keep
class FileRemote : Parcelable, ParcelNative.ParcelableNative {
    @SerializedName("id")
    var id = 0
        private set

    @SerializedName("owner_Id")
    var owner_Id = 0
        private set

    @SerializedName("file_name")
    var file_name: String? = null
        private set

    @SerializedName("type")
    @FileType
    var type: Int = FileType.error
        private set

    @SerializedName("modification_time")
    var modification_time: Long = 0
        private set

    @SerializedName("size")
    var size: Long = 0
        private set

    @SerializedName("url")
    var url: String? = null
        private set

    @SerializedName("preview_url")
    var preview_url: String? = null
        private set

    @SerializedName("is_selected")
    var isSelected: Boolean = false
        private set

    @Suppress("UNUSED")
    constructor()
    private constructor(`in`: Parcel) {
        id = `in`.readInt()
        owner_Id = `in`.readInt()
        file_name = `in`.readString()
        type = `in`.readInt()
        modification_time = `in`.readLong()
        size = `in`.readLong()
        url = `in`.readString()
        preview_url = `in`.readString()
        isSelected = `in`.readByte() != 0.toByte()
    }

    private constructor(`in`: ParcelNative) {
        id = `in`.readInt()
        owner_Id = `in`.readInt()
        file_name = `in`.readString()
        type = `in`.readInt()
        modification_time = `in`.readLong()
        size = `in`.readLong()
        url = `in`.readString()
        preview_url = `in`.readString()
        isSelected = `in`.readBoolean()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(owner_Id)
        dest.writeString(file_name)
        dest.writeInt(type)
        dest.writeLong(modification_time)
        dest.writeLong(size)
        dest.writeString(url)
        dest.writeString(preview_url)
        dest.writeByte(if (isSelected) 1.toByte() else 0.toByte())
    }

    override fun writeToParcelNative(dest: ParcelNative) {
        dest.writeInt(id)
        dest.writeInt(owner_Id)
        dest.writeString(file_name)
        dest.writeInt(type)
        dest.writeLong(modification_time)
        dest.writeLong(size)
        dest.writeString(url)
        dest.writeString(preview_url)
        dest.writeBoolean(isSelected)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun setId(id: Int): FileRemote {
        this.id = id
        return this
    }

    fun setOwnerId(ownerId: Int): FileRemote {
        this.owner_Id = ownerId
        return this
    }

    fun setFileName(title: String?): FileRemote {
        this.file_name = title
        return this
    }

    fun setType(@FileType type: Int): FileRemote {
        this.type = type
        return this
    }

    fun setModTime(modification_time: Long): FileRemote {
        this.modification_time = modification_time
        return this
    }

    fun setSize(size: Long): FileRemote {
        this.size = size
        return this
    }

    fun setUrl(url: String): FileRemote {
        this.url = url
        return this
    }

    fun setPreview(preview_url: String): FileRemote {
        this.preview_url = preview_url
        return this
    }

    fun setSelected(sel: Boolean): FileRemote {
        this.isSelected = sel
        return this
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FileRemote> = object : Parcelable.Creator<FileRemote> {
            override fun createFromParcel(`in`: Parcel): FileRemote {
                return FileRemote(`in`)
            }

            override fun newArray(size: Int): Array<FileRemote?> {
                return arrayOfNulls(size)
            }
        }
        val NativeCreator: ParcelNative.Creator<FileRemote> =
            object : ParcelNative.Creator<FileRemote> {
                override fun readFromParcelNative(dest: ParcelNative): FileRemote {
                    return FileRemote(dest)
                }
            }
    }
}
