package dev.ragnarok.filegallery.util;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Property;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import dev.ragnarok.filegallery.R;
import dev.ragnarok.filegallery.settings.CurrentTheme;

public class ViewUtils {

    private static final ICountFormatter DEFAULT_COUNT_FORMATTER = String::valueOf;

    public static ObjectAnimator setCountText(TextView view, int count, boolean animate) {
        if (Objects.nonNull(view)) {
            if (animate) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, createAmountAnimatorProperty(DEFAULT_COUNT_FORMATTER), count);
                animator.setDuration(250);
                animator.start();
                return animator;
            } else {
                view.setTag(count);
                view.setText(DEFAULT_COUNT_FORMATTER.format(count));
            }
        }

        return null;
    }

    private static Property<TextView, Integer> createAmountAnimatorProperty(ICountFormatter formatter) {
        return new Property<TextView, Integer>(Integer.class, "counter_text") {
            @Override
            public Integer get(TextView view) {
                try {
                    return (Integer) view.getTag();
                } catch (Exception e) {
                    return 0;
                }
            }

            @Override
            public void set(TextView view, Integer value) {
                view.setText(formatter.format(value));
                view.setTag(value);
            }
        };
    }

    public static void setupSwipeRefreshLayoutWithCurrentTheme(Activity activity, SwipeRefreshLayout swipeRefreshLayout) {
        setupSwipeRefreshLayoutWithCurrentTheme(activity, swipeRefreshLayout, false);
    }

    public static void setupSwipeRefreshLayoutWithCurrentTheme(Activity activity, SwipeRefreshLayout swipeRefreshLayout, boolean needToolbarOffset) {
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(CurrentTheme.getColorSurface(activity));

        int primaryColor = CurrentTheme.getColorPrimary(activity);
        int accentColor = CurrentTheme.getColorSecondary(activity);
        swipeRefreshLayout.setColorSchemeColors(primaryColor, accentColor);
        if (needToolbarOffset) {
            swipeRefreshLayout.setProgressViewOffset(false,
                    activity.getResources().getDimensionPixelSize(R.dimen.refresher_offset_start),
                    activity.getResources().getDimensionPixelSize(R.dimen.refresher_offset_end));
        }
    }

    public static void showProgress(@NonNull Fragment fragment, SwipeRefreshLayout swipeRefreshLayout, boolean show) {
        if (!fragment.isAdded() || swipeRefreshLayout == null) return;

        if (!show) {
            swipeRefreshLayout.setRefreshing(false);
            return;
        }

        if (fragment.isResumed()) {
            swipeRefreshLayout.setRefreshing(true);
        } else {
            swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
        }
    }

    public static void keyboardHide(Context context) {
        try {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager != null) {
                inputManager.hideSoftInputFromWindow(((Activity) context).getWindow().getDecorView().getRootView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } catch (Exception ignored) {
        }
    }

    public interface ICountFormatter {
        String format(int count);
    }
}
