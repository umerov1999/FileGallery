package dev.ragnarok.filegallery.activity

import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dev.ragnarok.filegallery.Includes.provideApplicationContext
import dev.ragnarok.filegallery.mvp.compat.AbsMvpActivity
import dev.ragnarok.filegallery.mvp.core.AbsPresenter
import dev.ragnarok.filegallery.mvp.core.IMvpView
import dev.ragnarok.filegallery.util.CustomToast
import dev.ragnarok.filegallery.util.CustomToast.Companion.CreateCustomToast
import dev.ragnarok.filegallery.util.ErrorLocalizer.localizeThrowable
import dev.ragnarok.filegallery.util.Utils
import dev.ragnarok.filegallery.util.ViewUtils

abstract class BaseMvpActivity<P : AbsPresenter<V>, V : IMvpView> : AbsMvpActivity<P, V>(),
    IMvpView {
    protected val arguments: Bundle?
        get() = intent?.extras

    protected fun requireArguments(): Bundle {
        return intent!!.extras!!
    }

    override fun showToast(@StringRes titleTes: Int, isLong: Boolean, vararg params: Any?) {
        if (!isFinishing) {
            Toast.makeText(
                this,
                getString(titleTes, *params),
                if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun showError(errorText: String?) {
        if (!isFinishing) {
            Utils.showRedTopToast(this, errorText)
        }
    }

    override fun showThrowable(throwable: Throwable?) {
        if (!isFinishing) {
            showError(localizeThrowable(provideApplicationContext(), throwable))
        }
    }

    override val customToast: CustomToast
        get() = if (!isFinishing) {
            CreateCustomToast(this)
        } else CreateCustomToast(null)

    override fun showError(@StringRes titleTes: Int, vararg params: Any?) {
        if (!isFinishing) {
            showError(getString(titleTes, *params))
        }
    }

    override fun setToolbarSubtitle(subtitle: String?) {
        supportActionBar?.subtitle = subtitle
    }

    override fun setToolbarTitle(title: String?) {
        supportActionBar?.title = title
    }

    protected fun styleSwipeRefreshLayoutWithCurrentTheme(
        swipeRefreshLayout: SwipeRefreshLayout,
        needToolbarOffset: Boolean
    ) {
        ViewUtils.setupSwipeRefreshLayoutWithCurrentTheme(
            this,
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