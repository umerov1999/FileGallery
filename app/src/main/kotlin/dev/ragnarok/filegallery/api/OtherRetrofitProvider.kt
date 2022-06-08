package dev.ragnarok.filegallery.api

import android.annotation.SuppressLint
import dev.ragnarok.filegallery.Constants
import dev.ragnarok.filegallery.nonNullNoEmpty
import dev.ragnarok.filegallery.settings.ISettings.IMainSettings
import dev.ragnarok.filegallery.util.Utils.firstNonEmptyString
import dev.ragnarok.filegallery.util.retrofit.gson.GsonConverterFactory
import dev.ragnarok.filegallery.util.retrofit.rxjava3.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Single
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

class OtherRetrofitProvider @SuppressLint("CheckResult") constructor(private val mainSettings: IMainSettings) :
    IOtherRetrofitProvider {
    private val localServerRetrofitLock = Any()
    private var localServerRetrofitInstance: RetrofitWrapper? = null
    private fun onLocalServerSettingsChanged() {
        synchronized(localServerRetrofitLock) {
            localServerRetrofitInstance?.cleanup()
            localServerRetrofitInstance = null
        }
    }

    private fun createLocalServerRetrofit(): Retrofit {
        val localSettings = mainSettings.getLocalServer()
        val builder = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLogger.DEFAULT_LOGGING_INTERCEPTOR)
            .addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val request =
                    chain.request().newBuilder().addHeader("User-Agent", Constants.USER_AGENT)
                        .build()
                chain.proceed(request)
            }).addInterceptor(Interceptor { chain: Interceptor.Chain ->
                val original = chain.request()
                val formBuilder = FormBody.Builder()
                val body = original.body
                if (body is FormBody) {
                    for (i in 0 until body.size) {
                        formBuilder.add(body.name(i), body.value(i))
                    }
                }
                localSettings.password.nonNullNoEmpty {
                    formBuilder.add("password", it)
                }
                val request = original.newBuilder()
                    .method("POST", formBuilder.build())
                    .build()
                chain.proceed(request)
            })
        val url = firstNonEmptyString(localSettings.url, "https://debug.dev")!!
        return Retrofit.Builder()
            .baseUrl("$url/method/")
            .addConverterFactory(GsonConverterFactory.create(RetrofitProvider.gson))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(builder.build())
            .build()
    }

    override fun provideLocalServerRetrofit(): Single<RetrofitWrapper> {
        return Single.fromCallable {
            if (localServerRetrofitInstance == null) {
                synchronized(localServerRetrofitLock) {
                    if (localServerRetrofitInstance == null) {
                        localServerRetrofitInstance =
                            RetrofitWrapper.wrap(createLocalServerRetrofit())
                    }
                }
            }
            localServerRetrofitInstance
        }
    }

    init {
        mainSettings.observeLocalServer()
            .subscribe { onLocalServerSettingsChanged() }
    }
}