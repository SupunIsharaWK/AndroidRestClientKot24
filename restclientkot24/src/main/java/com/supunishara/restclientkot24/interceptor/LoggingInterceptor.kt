package com.supunishara.restclientkot24.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import android.util.Log

class LoggingInterceptor : Interceptor {
    private val logger = HttpLoggingInterceptor.Logger { message ->
        Log.d("RestClient", message)
    }

    private val delegate = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        return delegate.intercept(chain)
    }
}