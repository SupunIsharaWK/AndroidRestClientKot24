package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.TempCookieJar.Companion.getInstance
import okhttp3.Cookie
import okhttp3.HttpUrl
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TempCookieJarTest {

    private lateinit var tempCookieJar: TempCookieJar
    private lateinit var mockUrl: HttpUrl
    private lateinit var mockCookie: Cookie
    private lateinit var cookieList: List<Cookie>

    @BeforeEach
    fun setUp() {
        tempCookieJar = getInstance() as TempCookieJar
        mockUrl = mock(HttpUrl::class.java)
        mockCookie = mock(Cookie::class.java)
        cookieList = listOf(mockCookie)
    }

    @AfterEach
    fun tearDown() {
        // No cleanup needed currently
    }

    @Test
    fun `saveFromResponse should store cookies for given URL`() {
        `when`(mockUrl.host).thenReturn("example.com")

        tempCookieJar.saveFromResponse(mockUrl, cookieList)

        val savedCookies = tempCookieJar.loadForRequest(mockUrl)
        assertEquals(cookieList, savedCookies)
    }

    @Test
    fun `loadForRequest should retrieve previously stored cookies`() {
        `when`(mockUrl.host).thenReturn("example.com")
        tempCookieJar.saveFromResponse(mockUrl, cookieList)

        val retrievedCookies = tempCookieJar.loadForRequest(mockUrl)
        assertEquals(cookieList, retrievedCookies)
    }

    @Test
    fun `loadForRequest should return empty list when no cookies found`() {
        `when`(mockUrl.host).thenReturn("unknown.com")

        val retrievedCookies = tempCookieJar.loadForRequest(mockUrl)
        assertTrue(retrievedCookies.isEmpty())
    }

    @Test
    fun `getInstance should return the same singleton instance`() {
        val instance1 = TempCookieJar.getInstance()
        val instance2 = TempCookieJar.getInstance()

        assertSame(instance1, instance2)
    }
}
