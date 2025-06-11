package com.supunishara.restclientkot24.network

import com.supunishara.restclientkot24.interceptor.HeaderInterceptor
import com.supunishara.restclientkot24.interceptor.LoggingInterceptor
import com.supunishara.restclientkot24.security.CertificatePinnerProvider
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Provides a preconfigured OkHttpClient instance with interceptors and certificate pinning.
 */
object OkHttpClientProvider {
    fun provide(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor())
            .addInterceptor(LoggingInterceptor())
            .certificatePinner(CertificatePinnerProvider.provide())
            .build()
    }
}