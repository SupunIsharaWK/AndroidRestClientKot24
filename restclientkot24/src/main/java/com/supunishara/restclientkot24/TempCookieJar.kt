package com.supunishara.restclientkot24

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * Temporary in-memory cookie store for session-based usage.
 * This implementation does not persist cookies across app restarts.
 *
 * Use only if [RestClientConfig.enableTempCookieJar] is true.
 */
object TempCookieJar : CookieJar {

    // Cookie storage mapped by host
    private val cookieStore: MutableMap<String, List<Cookie>> = mutableMapOf()

    /**
     * Called by OkHttp after receiving response cookies from server.
     */
    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    /**
     * Called before sending a request to attach any stored cookies.
     */
    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url.host] ?: emptyList()
    }
}
