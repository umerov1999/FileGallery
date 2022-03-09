package dev.ragnarok.filegallery.util;

import static dev.ragnarok.filegallery.util.Objects.isNull;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.graphics.ColorUtils;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import dev.ragnarok.filegallery.BuildConfig;
import dev.ragnarok.filegallery.Constants;
import dev.ragnarok.filegallery.Includes;
import dev.ragnarok.filegallery.R;
import dev.ragnarok.filegallery.media.exo.OkHttpDataSource;
import dev.ragnarok.filegallery.module.rlottie.RLottieDrawable;
import dev.ragnarok.filegallery.settings.CurrentTheme;
import dev.ragnarok.filegallery.settings.Settings;
import dev.ragnarok.filegallery.view.natives.rlottie.RLottieImageView;
import dev.ragnarok.filegallery.view.pager.BackgroundToForegroundTransformer;
import dev.ragnarok.filegallery.view.pager.ClockSpinTransformer;
import dev.ragnarok.filegallery.view.pager.CubeInDepthTransformer;
import dev.ragnarok.filegallery.view.pager.DepthTransformer;
import dev.ragnarok.filegallery.view.pager.FanTransformer;
import dev.ragnarok.filegallery.view.pager.GateTransformer;
import dev.ragnarok.filegallery.view.pager.SliderTransformer;
import dev.ragnarok.filegallery.view.pager.Transformers_Types;
import dev.ragnarok.filegallery.view.pager.ZoomOutTransformer;
import io.reactivex.rxjava3.core.Completable;
import okhttp3.Call;
import okhttp3.OkHttpClient;

public class Utils {
    private static final Point displaySize = new Point();
    private static float density = 1;

    public static String stringEmptyIfNull(String orig) {
        return orig == null ? "" : orig;
    }

    public static boolean nonEmpty(Collection<?> data) {
        return data != null && !data.isEmpty();
    }

    public static Throwable getCauseIfRuntime(Throwable throwable) {
        Throwable target = throwable;
        while (target instanceof RuntimeException) {
            if (isNull(target.getCause())) {
                break;
            }

            target = target.getCause();
        }

        return target;
    }

    public static boolean isEmpty(CharSequence body) {
        return body == null || body.length() == 0;
    }

    public static boolean nonEmpty(CharSequence text) {
        return text != null && text.length() > 0;
    }

    public static boolean isEmpty(Collection<?> data) {
        return data == null || data.isEmpty();
    }

    public static boolean isEmpty(Map<?, ?> data) {
        return data == null || data.size() == 0;
    }


    public static boolean hasMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }

    public static boolean hasScopedStorage() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && BuildConfig.MANAGE_SCOPED_STORAGE;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }

    public static boolean hasOreo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    public static boolean hasPie() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.P;
    }

    public static boolean hasR() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.R;
    }

    public static String firstNonEmptyString(String... array) {
        for (String s : array) {
            if (!TextUtils.isEmpty(s)) {
                return s;
            }
        }

        return null;
    }

    @SafeVarargs
    public static <T> T firstNonNull(T... items) {
        for (T t : items) {
            if (t != null) {
                return t;
            }
        }

        return null;
    }

    public static boolean safeIsEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static int safeCountOf(Collection<?> collection) {
        return collection == null ? 0 : collection.size();
    }

    /**
     * Добавляет прозрачность к цвету
     *
     * @param color  цвет
     * @param factor степень прозрачности
     * @return прозрачный цвет
     */
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static void setTint(@Nullable ImageView view, @ColorInt int color) {
        if (isNull(view)) {
            return;
        }
        view.setImageTintList(ColorStateList.valueOf(color));
    }

    public static void setBackgroundTint(@Nullable ImageView view, @ColorInt int color) {
        if (isNull(view)) {
            return;
        }
        view.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    public static void setColorFilter(@Nullable ImageView view, @ColorInt int color) {
        if (isNull(view)) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            view.setColorFilter(new BlendModeColorFilter(color, BlendMode.MODULATE));
        } else {
            view.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }

    public static void doWavesLottie(RLottieImageView visual, boolean Play) {
        visual.clearAnimationDrawable();
        if (Play) {
            visual.setAutoRepeat(true);
            visual.fromRes(R.raw.waves, dp(28), dp(28));
        } else {
            visual.setAutoRepeat(false);
            visual.fromRes(R.raw.waves_end, dp(28), dp(28));
        }
        visual.playAnimation();
    }

    public static void doWavesLottieBig(RLottieImageView visual, boolean Play) {
        visual.clearAnimationDrawable();
        if (Play) {
            visual.setAutoRepeat(true);
            visual.fromRes(R.raw.s_waves, dp(128), dp(128));
        } else {
            visual.setAutoRepeat(false);
            visual.fromRes(R.raw.s_waves_end, dp(128), dp(128));
        }
        visual.playAnimation();
    }

    public static boolean isColorDark(int color) {
        return ColorUtils.calculateLuminance(color) < 0.5;
    }

    public static float getDensity() {
        return density;
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static float dpf2(float value) {
        if (value == 0) {
            return 0;
        }
        return density * value;
    }

    public static void prepareDensity(Context context) {
        density = context.getResources().getDisplayMetrics().density;
        Display display = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display = context.getDisplay();
        } else {
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                display = manager.getDefaultDisplay();
            }
        }
        if (display != null) {
            RLottieDrawable.updateScreenRefreshRate((int) display.getRefreshRate());

            Configuration configuration = context.getResources().getConfiguration();
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenWidthDp * density);
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize;
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                int newSize = (int) Math.ceil(configuration.screenHeightDp * density);
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize;
                }
            }
        }
    }

    public static float clamp(float value, float min, float max) {
        if (value > max) {
            return max;
        } else if (value < min) {
            return min;
        }
        return value;
    }

    @SuppressLint("CheckResult")
    public static void inMainThread(@NonNull safeCallInt function) {
        Completable.complete()
                .observeOn(Includes.provideMainThreadScheduler())
                .subscribe(function::call);
    }


    public static OkHttpClient.Builder createOkHttp(int timeouts) {
        return new OkHttpClient.Builder()
                .connectTimeout(timeouts, TimeUnit.SECONDS)
                .readTimeout(timeouts, TimeUnit.SECONDS)
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().addHeader("User-Agent", Constants.USER_AGENT).build()));
    }

    public static boolean checkValues(Collection<Boolean> values) {
        for (Boolean i : values) {
            if (!i) {
                return false;
            }
        }
        return true;
    }


    public static int makeMutablePendingIntent(int flags) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (flags == 0) {
                return PendingIntent.FLAG_MUTABLE;
            } else {
                return flags | PendingIntent.FLAG_MUTABLE;
            }
        }
        return flags;
    }

    public static String BytesToSize(long Bytes) {
        long tb = 1099511627776L;
        long gb = 1073741824;
        long mb = 1048576;
        long kb = 1024;

        String returnSize;
        if (Bytes >= tb)
            returnSize = String.format(Locale.getDefault(), "%.2f TB", (double) Bytes / tb);
        else if (Bytes >= gb)
            returnSize = String.format(Locale.getDefault(), "%.2f GB", (double) Bytes / gb);
        else if (Bytes >= mb)
            returnSize = String.format(Locale.getDefault(), "%.2f MB", (double) Bytes / mb);
        else if (Bytes >= kb)
            returnSize = String.format(Locale.getDefault(), "%.2f KB", (double) Bytes / kb);
        else returnSize = String.format(Locale.getDefault(), "%d Bytes", Bytes);
        return returnSize;
    }

    public static Context updateActivityContext(Context base) {
        int size = Settings.INSTANCE.get().main().getFontSize();
        if (size == 0) {
            return base;
        } else {
            Resources res = base.getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.fontScale = res.getConfiguration().fontScale + 0.15f * size;
            return base.createConfigurationContext(config);
        }
    }

    public static void showRedTopToast(@NonNull Context activity, String text) {
        View view = View.inflate(activity, R.layout.toast_error, null);
        ((TextView) view.findViewById(R.id.text)).setText(text);

        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 15);
        toast.show();
    }

    public static void showRedTopToast(@NonNull Context activity, @StringRes int text, Object... params) {
        View view = View.inflate(activity, R.layout.toast_error, null);
        ((TextView) view.findViewById(R.id.text)).setText(activity.getString(text, params));

        Toast toast = Toast.makeText(activity, text, Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 15);
        toast.show();
    }

    @NonNull
    public static Snackbar ThemedSnack(@NonNull View view, @StringRes int resId, @BaseTransientBottomBar.Duration int duration) {
        return ThemedSnack(view, view.getResources().getText(resId), duration);
    }

    @NonNull
    public static Snackbar ThemedSnack(@NonNull View view, @NonNull CharSequence text, @BaseTransientBottomBar.Duration int duration) {
        int color = CurrentTheme.getColorPrimary(view.getContext());
        int text_color = isColorDark(color)
                ? Color.parseColor("#ffffff") : Color.parseColor("#000000");

        return Snackbar.make(view, text, duration).setBackgroundTint(color).setActionTextColor(text_color).setTextColor(text_color);
    }

    @NonNull
    public static Snackbar ColoredSnack(@NonNull View view, @StringRes int resId, @BaseTransientBottomBar.Duration int duration, @ColorInt int color) {
        return ColoredSnack(view, view.getResources().getText(resId), duration, color);
    }

    @NonNull
    public static Snackbar ColoredSnack(@NonNull View view, @NonNull CharSequence text, @BaseTransientBottomBar.Duration int duration, @ColorInt int color) {
        int text_color = isColorDark(color)
                ? Color.parseColor("#ffffff") : Color.parseColor("#000000");

        return Snackbar.make(view, text, duration).setBackgroundTint(color).setActionTextColor(text_color).setTextColor(text_color);
    }

    public static @NonNull
    OkHttpDataSource.Factory getExoPlayerFactory(String userAgent) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS);
        return new OkHttpDataSource.Factory((Call.Factory) builder.build()).setUserAgent(userAgent);
    }

    public static MediaItem makeMediaItem(String url) {
        return new MediaItem.Builder().setUri(url).build();
    }

    public static void showErrorInAdapter(Activity context, Throwable throwable) {
        if (context == null || context.isFinishing() || context.isDestroyed()) {
            return;
        }
        throwable = getCauseIfRuntime(throwable);
        if (Constants.IS_DEBUG) {
            throwable.printStackTrace();
        }
        showRedTopToast(context, ErrorLocalizer.localizeThrowable(context.getApplicationContext(), throwable));
    }

    @Nullable
    public static ViewPager2.PageTransformer createPageTransform(@Transformers_Types int type) {
        switch (type) {
            case Transformers_Types.SLIDER_TRANSFORMER:
                return new SliderTransformer(1);
            case Transformers_Types.CLOCK_SPIN_TRANSFORMER:
                return new ClockSpinTransformer();
            case Transformers_Types.BACKGROUND_TO_FOREGROUND_TRANSFORMER:
                return new BackgroundToForegroundTransformer();
            case Transformers_Types.CUBE_IN_DEPTH_TRANSFORMER:
                return new CubeInDepthTransformer();
            case Transformers_Types.DEPTH_TRANSFORMER:
                return new DepthTransformer();
            case Transformers_Types.FAN_TRANSFORMER:
                return new FanTransformer();
            case Transformers_Types.GATE_TRANSFORMER:
                return new GateTransformer();
            case Transformers_Types.OFF:
                return null;
            case Transformers_Types.ZOOM_OUT_TRANSFORMER:
                return new ZoomOutTransformer();
        }
        return null;
    }

    public interface safeCallInt {
        void call();
    }

}
