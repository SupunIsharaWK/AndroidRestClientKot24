package com.supunishara.restclientkot24

import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [28],
    manifest = Config.NONE
)  //@Config(sdk = [33]) // or any API level you want to simulate
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
        TempCookieJar.saveFromResponse(url, listOf(cookie))

        val loadedCookies = TempCookieJar.loadForRequest(url)
        assertNotNull(loadedCookies)
        assertEquals(1, loadedCookies.size)
        assertEquals("session", loadedCookies[0].name)
        assertEquals("abc123", loadedCookies[0].value)
    }

    @Test
    fun testLoadForRequestReturnsEmptyIfNoCookiesSaved() {
        val newUrl = "https://another.com".toHttpUrlOrNull()!!
        val cookies = TempCookieJar.loadForRequest(newUrl)
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

        TempCookieJar.saveFromResponse(url, listOf(cookie))
        TempCookieJar.saveFromResponse(
            url,
            listOf(newCookie)
        ) // overwrite

        val cookies = TempCookieJar.loadForRequest(url)
        assertEquals(1, cookies.size)
        assertEquals("xyz789", cookies[0].value)
    }
}
