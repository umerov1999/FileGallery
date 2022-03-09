package dev.ragnarok.filegallery.fragment.base

import android.graphics.Color
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.ragnarok.filegallery.Includes.provideApplicationContext
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.activity.ActivityUtils.setToolbarSubtitle
import dev.ragnarok.filegallery.activity.ActivityUtils.setToolbarTitle
import dev.ragnarok.filegallery.mvp.compat.AbsMvpBottomSheetDialogFragment
import dev.ragnarok.filegallery.mvp.core.AbsPresenter
import dev.ragnarok.filegallery.mvp.core.IMvpView
import dev.ragnarok.filegallery.mvp.view.IErrorView
import dev.ragnarok.filegallery.mvp.view.IToastView
import dev.ragnarok.filegallery.mvp.view.IToolbarView
import dev.ragnarok.filegallery.util.CustomToast
import dev.ragnarok.filegallery.util.CustomToast.Companion.CreateCustomToast
import dev.ragnarok.filegallery.util.ErrorLocalizer.localizeThrowable
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.util.ViewUtils

abstract class BaseMvpBottomSheetDialogFragment<P : AbsPresenter<V>, V : IMvpView> :
    AbsMvpBottomSheetDialogFragment<P, V>(), IMvpView, IErrorView, IToastView, IToolbarView {
    override fun showToast(@StringRes titleTes: Int, isLong: Boolean, vararg params: Any?) {
        if (isAdded) {
            Toast.makeText(
                requireActivity(),
                getString(titleTes, *params),
                if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun showError(errorText: String?) {
        if (isAdded) {
            Utils.showRedTopToast(requireActivity(), errorText)
        }
    }

    override val customToast: CustomToast
        get() = if (isAdded) {
            CreateCustomToast(requireActivity())
        } else CreateCustomToast(null)

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
                    localizeThrowable(provideApplicationContext(), throwable),
                    BaseTransientBottomBar.LENGTH_LONG
                ).setTextColor(
                    Color.WHITE
                ).setBackgroundTint(Color.parseColor("#eeff0000"))
                    .setAction(R.string.more_info) {
                        val Text = StringBuilder()
                        for (stackTraceElement in throwable!!.stackTrace) {
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
            } ?: showError(localizeThrowable(provideApplicationContext(), throwable))
        }
    }

    override fun setToolbarSubtitle(subtitle: String?) {
        setToolbarSubtitle(this, subtitle)
    }

    override fun setToolbarTitle(title: String?) {
        setToolbarTitle(this, title)
    }

    protected fun styleSwipeRefreshLayoutWithCurrentTheme(
        swipeRefreshLayout: SwipeRefreshLayout,
        needToolbarOffset: Boolean
    ) {
        ViewUtils.setupSwipeRefreshLayoutWithCurrentTheme(
            requireActivity(),
            swipeRefreshLayout,
            needToolbarOffset
        )
    }

    companion object {
        const val EXTRA_HIDE_TOOLBAR = "extra_hide_toolbar"
        protected fun safelySetChecked(button: CompoundButton?, checked: Boolean) {
            button?.isChecked = checked
        }

        protected fun safelySetText(target: TextView?, text: String?) {
            target?.text = text
        }

        protected fun safelySetText(target: TextView?, @StringRes text: Int) {
            target?.setText(text)
        }

        protected fun safelySetVisibleOrGone(target: View?, visible: Boolean) {
            target?.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }
}