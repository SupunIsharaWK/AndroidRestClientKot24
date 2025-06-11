package com.supunishara.restclientkot24.interceptor

import com.supunishara.restclientkot24.security.AuthTokenProvider
import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(
    private val headers: Map<String, String> = emptyMap(),
    private val tokenProvider: AuthTokenProvider,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        tokenProvider.getToken()?.let { token ->
            requestBuilder.addHeader("Authorization", "Bearer $token")
        }

        headers.forEach { (key, value) ->
            requestBuilder.addHeader(key, value)
        }

        return chain.proceed(requestBuilder.build())
    }
}
