package dev.ragnarok.filegallery.mvp.view

import androidx.annotation.StringRes
import dev.ragnarok.filegallery.util.CustomToast

interface IErrorView {
    fun showError(errorText: String?)
    fun showThrowable(throwable: Throwable?)
    fun showError(@StringRes titleTes: Int, vararg params: Any?)
    val customToast: CustomToast?
}