package dev.ragnarok.filegallery.api;

import static dev.ragnarok.filegallery.util.Objects.nonNull;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

import dev.ragnarok.filegallery.Constants;
import dev.ragnarok.filegallery.model.LocalServerSettings;
import dev.ragnarok.filegallery.settings.ISettings;
import dev.ragnarok.filegallery.util.Objects;
import dev.ragnarok.filegallery.util.Utils;
import io.reactivex.rxjava3.core.Single;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class OtherVkRetrofitProvider implements IOtherVkRetrofitProvider {

    private final ISettings.IMainSettings mainSettings;
    private final Object localServerRetrofitLock = new Object();
    private RetrofitWrapper localServerRetrofitInstance;

    @SuppressLint("CheckResult")
    public OtherVkRetrofitProvider(ISettings.IMainSettings mainSettings) {
        this.mainSettings = mainSettings;
        this.mainSettings.observeLocalServer()
                .subscribe(ignored -> onLocalServerSettingsChanged());
    }

    private void onLocalServerSettingsChanged() {
        synchronized (localServerRetrofitLock) {
            if (nonNull(localServerRetrofitInstance)) {
                localServerRetrofitInstance.cleanup();
                localServerRetrofitInstance = null;
            }
        }
    }

    private Retrofit createLocalServerRetrofit() {
        LocalServerSettings local_settings = mainSettings.getLocalServer();
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(HttpLogger.DEFAULT_LOGGING_INTERCEPTOR).addInterceptor(chain -> {
                    Request request = chain.request().newBuilder().addHeader("User-Agent", Constants.USER_AGENT).build();
                    return chain.proceed(request);
                }).addInterceptor(chain -> {
                    Request original = chain.request();
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    RequestBody body = original.body();
                    if (body instanceof FormBody) {
                        FormBody formBody = (FormBody) body;
                        for (int i = 0; i < formBody.size(); i++) {
                            formBuilder.add(formBody.name(i), formBody.value(i));
                        }
                    }
                    if (local_settings.password != null) {
                        formBuilder.add("password", local_settings.password);
                    }
                    Request request = original.newBuilder()
                            .method("POST", formBuilder.build())
                            .build();
                    return chain.proceed(request);
                });
        String url = Utils.firstNonEmptyString(local_settings.url, "https://debug.dev");
        assert url != null;
        return new Retrofit.Builder()
                .baseUrl(url + "/method/")
                .addConverterFactory(GsonConverterFactory.create(VkRetrofitProvider.getVkgson()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .client(builder.build())
                .build();
    }

    @Override
    public Single<RetrofitWrapper> provideLocalServerRetrofit() {
        return Single.fromCallable(() -> {

            if (Objects.isNull(localServerRetrofitInstance)) {
                synchronized (localServerRetrofitLock) {
                    if (Objects.isNull(localServerRetrofitInstance)) {
                        localServerRetrofitInstance = RetrofitWrapper.wrap(createLocalServerRetrofit());
                    }
                }
            }

            return localServerRetrofitInstance;
        });
    }
}
