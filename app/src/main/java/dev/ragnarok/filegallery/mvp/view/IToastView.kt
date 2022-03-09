package dev.ragnarok.filegallery.mvp.view

import androidx.annotation.StringRes
import dev.ragnarok.filegallery.util.CustomToast

interface IToastView {
    fun showToast(@StringRes titleTes: Int, isLong: Boolean, vararg params: Any?)
    val customToast: CustomToast?
}