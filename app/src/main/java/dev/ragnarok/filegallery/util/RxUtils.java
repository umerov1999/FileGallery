package dev.ragnarok.filegallery.util;

import dev.ragnarok.filegallery.Constants;
import dev.ragnarok.filegallery.Includes;
import io.reactivex.rxjava3.core.CompletableTransformer;
import io.reactivex.rxjava3.core.ObservableTransformer;
import io.reactivex.rxjava3.core.SingleTransformer;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class RxUtils {

    private static final Action DUMMMY_ACTION_0 = () -> {
    };

    public static Action dummy() {
        return DUMMMY_ACTION_0;
    }

    public static <T> Consumer<T> ignore() {
        return t -> {
            if (t instanceof Throwable && Constants.IS_DEBUG) {
                ((Throwable) t).printStackTrace();
            }
        };
    }

    public static <T> SingleTransformer<T, T> applySingleIOToMainSchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Includes.provideMainThreadScheduler());
    }

    public static <T> SingleTransformer<T, T> applySingleComputationToMainSchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.computation())
                .observeOn(Includes.provideMainThreadScheduler());
    }

    public static <T> ObservableTransformer<T, T> applyObservableIOToMainSchedulers() {
        return upstream -> upstream
                .subscribeOn(Schedulers.io())
                .observeOn(Includes.provideMainThreadScheduler());
    }


    public static CompletableTransformer applyCompletableIOToMainSchedulers() {
        return completable -> completable.subscribeOn(Schedulers.io())
                .observeOn(Includes.provideMainThreadScheduler());
    }
}
