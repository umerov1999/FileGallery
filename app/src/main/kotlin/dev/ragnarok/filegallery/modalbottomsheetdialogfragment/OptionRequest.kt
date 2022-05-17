package dev.ragnarok.filegallery.modalbottomsheetdialogfragment

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

/**
 * Request for an option you can select within the modal
 */
class OptionRequest(
    val id: Int,
    val title: String?,
    @DrawableRes val icon: Int?,
    val singleLine: Boolean
) : Parcelable {

    internal fun toOption(context: Context): Option {
        var drawable: Drawable? = null
        icon?.let {
            drawable = ResourcesCompat.getDrawable(context.resources, icon, context.theme)
        }

        return Option(id, title, drawable, singleLine)
    }

    constructor(source: Parcel) : this(
        source.readInt(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readByte() == 1.toByte()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(title)
        writeValue(icon)
        writeByte(if (singleLine) 1.toByte() else 0.toByte())
    }

    companion object CREATOR : Parcelable.Creator<OptionRequest> {
        override fun createFromParcel(parcel: Parcel): OptionRequest {
            return OptionRequest(parcel)
        }

        override fun newArray(size: Int): Array<OptionRequest?> {
            return arrayOfNulls(size)
        }
    }
}
