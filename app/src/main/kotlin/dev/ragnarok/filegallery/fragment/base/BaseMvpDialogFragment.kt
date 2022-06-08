package dev.ragnarok.filegallery.fragment.base

import android.graphics.Color
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.ragnarok.filegallery.Includes.provideApplicationContext
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.mvp.compat.AbsMvpDialogFragment
import dev.ragnarok.filegallery.mvp.core.AbsPresenter
import dev.ragnarok.filegallery.mvp.core.IMvpView
import dev.ragnarok.filegallery.mvp.view.IErrorView
import dev.ragnarok.filegallery.mvp.view.IToastView
import dev.ragnarok.filegallery.util.CustomToast
import dev.ragnarok.filegallery.util.CustomToast.Companion.CreateCustomToast
import dev.ragnarok.filegallery.util.ErrorLocalizer
import dev.ragnarok.filegallery.util.Utils

abstract class BaseMvpDialogFragment<P : AbsPresenter<V>, V : IMvpView> :
    AbsMvpDialogFragment<P, V>(), IMvpView, IErrorView, IToastView {

    override fun showError(errorText: String?) {
        if (isAdded) {
            Utils.showRedTopToast(requireActivity(), errorText)
        }
    }

    override fun showError(@StringRes titleTes: Int, vararg params: Any?) {
        if (isAdded) {
            showError(getString(titleTes, *params))
        }
    }

    override fun showThrowable(throwable: Throwable?) {
        if (isAdded) {
            view?.let {
                Snackbar.make(
                    it,
                    ErrorLocalizer.localizeThrowable(provideApplicationContext(), throwable),
                    BaseTransientBottomBar.LENGTH_LONG
                ).setTextColor(
                    Color.WHITE
                ).setBackgroundTint(Color.parseColor("#eeff0000"))
                    .setAction(R.string.more_info) {
                        val Text = StringBuilder()
                        for (stackTraceElement in (throwable ?: return@setAction).stackTrace) {
                            Text.append("    ")
                            Text.append(stackTraceElement)
                            Text.append("\r\n")
                        }
                        MaterialAlertDialogBuilder(requireActivity())
                            .setIcon(R.drawable.ic_error)
                            .setMessage(Text)
                            .setTitle(R.string.more_info)
                            .setPositiveButton(R.string.button_ok, null)
                            .setCancelable(true)
                            .show()
                    }.setActionTextColor(Color.WHITE).show()
            } ?: showError(
                ErrorLocalizer.localizeThrowable(
                    provideApplicationContext(),
                    throwable
                )
            )
        }
    }

    override val customToast: CustomToast
        get() = if (isAdded) {
            CreateCustomToast(requireActivity())
        } else CreateCustomToast(null)
}