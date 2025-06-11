package com.supunishara.restclientkot24

import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TempCookieJarTest {

    private lateinit var url: HttpUrl
    private lateinit var cookie: Cookie

    @Before
    fun setup() {
        url = "https://example.com".toHttpUrlOrNull()!!
        cookie = Cookie.Builder()
            .name("session")
            .value("abc123")
            .domain("example.com")
            .build()
    }

    @Test
    fun testSaveAndLoadCookies() {
        com.supunishara.restclientkot24.TempCookieJar.saveFromResponse(url, listOf(cookie))

        val loadedCookies = com.supunishara.restclientkot24.TempCookieJar.loadForRequest(url)
        assertNotNull(loadedCookies)
        assertEquals(1, loadedCookies.size)
        assertEquals("session", loadedCookies[0].name)
        assertEquals("abc123", loadedCookies[0].value)
    }

    @Test
    fun testLoadForRequestReturnsEmptyIfNoCookiesSaved() {
        val newUrl = "https://another.com".toHttpUrlOrNull()!!
        val cookies = com.supunishara.restclientkot24.TempCookieJar.loadForRequest(newUrl)
        assertNotNull(cookies)
        assertTrue(cookies.isEmpty())
    }

    @Test
    fun testCookiesAreOverwrittenForSameHost() {
        val newCookie = Cookie.Builder()
            .name("session")
            .value("xyz789")
            .domain("example.com")
            .build()

        com.supunishara.restclientkot24.TempCookieJar.saveFromResponse(url, listOf(cookie))
        com.supunishara.restclientkot24.TempCookieJar.saveFromResponse(url, listOf(newCookie)) // overwrite

        val cookies = com.supunishara.restclientkot24.TempCookieJar.loadForRequest(url)
        assertEquals(1, cookies.size)
        assertEquals("xyz789", cookies[0].value)
    }
}
