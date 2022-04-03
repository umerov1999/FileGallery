package dev.ragnarok.filegallery.api

import dev.ragnarok.filegallery.Constants
import okhttp3.logging.HttpLoggingInterceptor

object HttpLogger {
    val DEFAULT_LOGGING_INTERCEPTOR = HttpLoggingInterceptor()

    init {
        if (Constants.IS_DEBUG) {
            DEFAULT_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            DEFAULT_LOGGING_INTERCEPTOR.setLevel(HttpLoggingInterceptor.Level.NONE)
        }
    }
}