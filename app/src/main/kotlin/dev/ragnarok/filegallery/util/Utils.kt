package dev.ragnarok.filegallery.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.*
import android.os.Build
import android.view.Display
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import androidx.core.graphics.ColorUtils
import androidx.viewpager2.widget.ViewPager2
import com.google.android.exoplayer2.MediaItem
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import dev.ragnarok.filegallery.BuildConfig
import dev.ragnarok.filegallery.Constants
import dev.ragnarok.filegallery.Includes.provideMainThreadScheduler
import dev.ragnarok.filegallery.R
import dev.ragnarok.filegallery.media.exo.OkHttpDataSource
import dev.ragnarok.filegallery.module.rlottie.RLottieDrawable
import dev.ragnarok.filegallery.settings.CurrentTheme.getColorPrimary
import dev.ragnarok.filegallery.settings.Settings.get
import dev.ragnarok.filegallery.util.ErrorLocalizer.localizeThrowable
import dev.ragnarok.filegallery.view.natives.rlottie.RLottieImageView
import dev.ragnarok.filegallery.view.pager.*
import io.reactivex.rxjava3.core.Completable
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.roundToInt

object Utils {
    private val displaySize = Point()
    var density = 1f
        private set

    fun stringEmptyIfNull(orig: String?): String {
        return orig ?: ""
    }

    fun getCauseIfRuntime(throwable: Throwable?): Throwable? {
        var target = throwable
        while (target is RuntimeException) {
            if (target.cause == null) {
                break
            }
            target.cause?.let {
                target = it
            }
        }
        return target
    }

    fun is600dp(context: Context): Boolean {
        return context.resources.getBoolean(R.bool.is_tablet)
    }

    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
    }

    fun hasScopedStorage(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && BuildConfig.MANAGE_SCOPED_STORAGE
    }

    fun hasNougat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    fun hasOreo(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    fun hasPie(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
    }

    fun hasR(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
    }

    fun firstNonEmptyString(vararg array: String?): String? {
        for (s in array) {
            if (!s.isNullOrEmpty()) {
                return s
            }
        }
        return null
    }

    @SafeVarargs
    fun <T> firstNonNull(vararg items: T): T? {
        for (t in items) {
            if (t != null) {
                return t
            }
        }
        return null
    }

    fun safeCountOf(collection: Collection<*>?): Int {
        return collection?.size ?: 0
    }

    /**
     * Добавляет прозрачность к цвету
     *
     * @param color  цвет
     * @param factor степень прозрачности
     * @return прозрачный цвет
     */
    fun adjustAlpha(color: Int, factor: Float): Int {
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    val deviceName: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else {
                capitalize(manufacturer) + " " + model
            }
        }

    private fun capitalize(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }
        val first = s[0]
        return if (Character.isUpperCase(first)) {
            s
        } else {
            Character.toUpperCase(first).toString() + s.substring(1)
        }
    }

    fun setTint(view: ImageView?, @ColorInt color: Int) {
        view?.imageTintList = ColorStateList.valueOf(color)
    }

    fun setBackgroundTint(view: ImageView?, @ColorInt color: Int) {
        view?.backgroundTintList = ColorStateList.valueOf(color)
    }

    fun setColorFilter(view: ImageView?, @ColorInt color: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view?.colorFilter = BlendModeColorFilter(color, BlendMode.MODULATE)
        } else {
            view?.setColorFilter(color, PorterDuff.Mode.MULTIPLY)
        }
    }

    fun doWavesLottie(visual: RLottieImageView, Play: Boolean) {
        visual.clearAnimationDrawable()
        if (Play) {
            visual.setAutoRepeat(true)
            visual.fromRes(R.raw.waves, dp(28f), dp(28f))
        } else {
            visual.setAutoRepeat(false)
            visual.fromRes(R.raw.waves_end, dp(28f), dp(28f))
        }
        visual.playAnimation()
    }

    fun doWavesLottieBig(visual: RLottieImageView, Play: Boolean) {
        visual.clearAnimationDrawable()
        if (Play) {
            visual.setAutoRepeat(true)
            visual.fromRes(R.raw.s_waves, dp(128f), dp(128f))
        } else {
            visual.setAutoRepeat(false)
            visual.fromRes(R.raw.s_waves_end, dp(128f), dp(128f))
        }
        visual.playAnimation()
    }

    fun isColorDark(color: Int): Boolean {
        return ColorUtils.calculateLuminance(color) < 0.5
    }

    fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else ceil((density * value).toDouble())
            .toInt()
    }

    fun dpf2(value: Float): Float {
        return if (value == 0f) {
            0f
        } else density * value
    }

    @Suppress("DEPRECATION")
    fun prepareDensity(context: Context) {
        density = context.resources.displayMetrics.density
        var display: Display? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display = context.display
        } else {
            val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
            if (manager != null) {
                display = manager.defaultDisplay
            }
        }
        if (display != null) {
            RLottieDrawable.updateScreenRefreshRate(display.refreshRate.toInt())
            val configuration = context.resources.configuration
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                val newSize = ceil((configuration.screenWidthDp * density).toDouble())
                    .toInt()
                if (abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                val newSize = ceil((configuration.screenHeightDp * density).toDouble())
                    .toInt()
                if (abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize
                }
            }
        }
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        if (value > max) {
            return max
        } else if (value < min) {
            return min
        }
        return value
    }

    @SuppressLint("CheckResult")
    fun inMainThread(function: SafeCall) {
        Completable.complete()
            .observeOn(provideMainThreadScheduler())
            .subscribe { function.call() }
    }

    fun createOkHttp(timeouts: Int): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .connectTimeout(timeouts.toLong(), TimeUnit.SECONDS)
            .readTimeout(timeouts.toLong(), TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                chain.proceed(
                    chain.request().newBuilder().addHeader("User-Agent", Constants.USER_AGENT)
                        .build()
                )
            })
    }

    fun checkValues(values: Collection<Boolean?>): Boolean {
        for (i in values) {
            if (i != true) {
                return false
            }
        }
        return true
    }

    fun makeMutablePendingIntent(flags: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (flags == 0) {
                PendingIntent.FLAG_MUTABLE
            } else {
                flags or PendingIntent.FLAG_MUTABLE
            }
        } else flags
    }

    fun BytesToSize(Bytes: Long): String {
        val tb = 1099511627776L
        val gb: Long = 1073741824
        val mb: Long = 1048576
        val kb: Long = 1024
        return when {
            Bytes >= tb -> String.format(
                Locale.getDefault(),
                "%.2f TB",
                Bytes.toDouble() / tb
            )
            Bytes >= gb -> String.format(
                Locale.getDefault(),
                "%.2f GB",
                Bytes.toDouble() / gb
            )
            Bytes >= mb -> String.format(
                Locale.getDefault(),
                "%.2f MB",
                Bytes.toDouble() / mb
            )
            Bytes >= kb -> String.format(
                Locale.getDefault(),
                "%.2f KB",
                Bytes.toDouble() / kb
            )
            else -> String.format(Locale.getDefault(), "%d Bytes", Bytes)
        }
    }

    fun updateActivityContext(base: Context): Context {
        val size = get().main().getFontSize()
        return if (size == 0) {
            base
        } else {
            val res = base.resources
            val config = Configuration(res.configuration)
            config.fontScale = res.configuration.fontScale + 0.15f * size
            base.createConfigurationContext(config)
        }
    }

    @Suppress("DEPRECATION")
    fun showRedTopToast(activity: Context, text: String?) {
        val view = View.inflate(activity, R.layout.toast_error, null)
        (view.findViewById<View>(R.id.text) as TextView).text = text
        val toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.view = view
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 15)
        toast.show()
    }

    @Suppress("DEPRECATION")
    fun showRedTopToast(activity: Context, @StringRes text: Int, vararg params: Any?) {
        val view = View.inflate(activity, R.layout.toast_error, null)
        (view.findViewById<View>(R.id.text) as TextView).text =
            activity.getString(text, *params)
        val toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
        toast.view = view
        toast.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP, 0, 15)
        toast.show()
    }

    fun ThemedSnack(
        view: View,
        @StringRes resId: Int,
        @BaseTransientBottomBar.Duration duration: Int
    ): Snackbar {
        return ThemedSnack(view, view.resources.getText(resId), duration)
    }

    fun ThemedSnack(
        view: View,
        text: CharSequence,
        @BaseTransientBottomBar.Duration duration: Int
    ): Snackbar {
        val color = getColorPrimary(view.context)
        val text_color =
            if (isColorDark(color)) Color.parseColor("#ffffff") else Color.parseColor("#000000")
        return Snackbar.make(view, text, duration).setBackgroundTint(color)
            .setActionTextColor(text_color).setTextColor(text_color)
    }

    fun ColoredSnack(
        view: View,
        @StringRes resId: Int,
        @BaseTransientBottomBar.Duration duration: Int,
        @ColorInt color: Int
    ): Snackbar {
        return ColoredSnack(view, view.resources.getText(resId), duration, color)
    }

    fun ColoredSnack(
        view: View,
        text: CharSequence,
        @BaseTransientBottomBar.Duration duration: Int,
        @ColorInt color: Int
    ): Snackbar {
        val text_color =
            if (isColorDark(color)) Color.parseColor("#ffffff") else Color.parseColor("#000000")
        return Snackbar.make(view, text, duration).setBackgroundTint(color)
            .setActionTextColor(text_color).setTextColor(text_color)
    }

    fun getExoPlayerFactory(userAgent: String?): OkHttpDataSource.Factory {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
        return OkHttpDataSource.Factory(builder.build()).setUserAgent(userAgent)
    }

    fun makeMediaItem(url: String?): MediaItem {
        return MediaItem.Builder().setUri(url).build()
    }

    fun showErrorInAdapter(context: Activity?, throwable: Throwable?) {
        var throwableData = throwable
        if (context == null || context.isFinishing || context.isDestroyed) {
            return
        }
        throwableData = getCauseIfRuntime(throwableData)
        if (Constants.IS_DEBUG) {
            throwableData?.printStackTrace()
        }
        showRedTopToast(context, localizeThrowable(context.applicationContext, throwableData))
    }

    fun createPageTransform(@Transformers_Types type: Int): ViewPager2.PageTransformer? {
        when (type) {
            Transformers_Types.SLIDER_TRANSFORMER -> return SliderTransformer(1)
            Transformers_Types.CLOCK_SPIN_TRANSFORMER -> return ClockSpinTransformer()
            Transformers_Types.BACKGROUND_TO_FOREGROUND_TRANSFORMER -> return BackgroundToForegroundTransformer()
            Transformers_Types.CUBE_IN_DEPTH_TRANSFORMER -> return CubeInDepthTransformer()
            Transformers_Types.DEPTH_TRANSFORMER -> return DepthTransformer()
            Transformers_Types.FAN_TRANSFORMER -> return FanTransformer()
            Transformers_Types.GATE_TRANSFORMER -> return GateTransformer()
            Transformers_Types.OFF -> return null
            Transformers_Types.ZOOM_OUT_TRANSFORMER -> return ZoomOutTransformer()
        }
        return null
    }

    interface SafeCall {
        fun call()
    }
}
