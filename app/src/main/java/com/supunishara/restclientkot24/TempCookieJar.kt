package com.supunishara.restclientkot24

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


@Suppress("UNCHECKED_CAST")
class TempCookieJar : CookieJar {
    private val cookieStore: HashMap<Any?, Any?> = HashMap<Any?, Any?>()
    private var tempCookieJar: TempCookieJar? = null

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        cookieStore[url.host] = cookies
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        val cookies: List<*>? =
            cookieStore[url.host] as List<*>?
        return ((cookies ?: ArrayList<Any?>()) as List<Cookie>)
    }

    fun getTempCookieJar(): TempCookieJar? {
        if (tempCookieJar == null) {
            tempCookieJar = TempCookieJar()
        }
        return tempCookieJar
    }

    companion object {
        fun getInstance(): Any {
            return tempCookieJar
        }

        lateinit var tempCookieJar: Any
    }
}