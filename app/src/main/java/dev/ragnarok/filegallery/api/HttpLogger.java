package dev.ragnarok.filegallery.api;

import dev.ragnarok.filegallery.BuildConfig;
import okhttp3.logging.HttpLoggingInterceptor;


public class HttpLogger {

    public static final HttpLoggingInterceptor DEFAULT_LOGGING_INTERCEPTOR = new HttpLoggingInterceptor();

    static {
        if (BuildConfig.DEBUG) {
            DEFAULT_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            DEFAULT_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
    }
}