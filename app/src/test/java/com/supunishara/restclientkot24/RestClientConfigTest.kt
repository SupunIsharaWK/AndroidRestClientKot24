package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestClientConfigTest {

    private lateinit var configBuilder: ConfigBuilder
    private lateinit var restClientConfig: RestClientConfig
    private lateinit var mockFile: File
    private lateinit var mockHeader: Header
    private lateinit var mockCookieJar: CookieJar
    private lateinit var mockHostnameVerifier: HostnameVerifier
    private lateinit var mockSslSocketFactory: SSLSocketFactory

    @BeforeEach
    fun setUp() {
        configBuilder = mock(ConfigBuilder::class.java)
        mockFile = mock(File::class.java)
        mockHeader = mock(Header::class.java)
        mockCookieJar = mock(CookieJar::class.java)
        mockHostnameVerifier = mock(HostnameVerifier::class.java)
        mockSslSocketFactory = mock(SSLSocketFactory::class.java)
    }

    @AfterEach
    fun tearDown() {
        // Optional cleanup
    }

    @Test
    fun `test default initialization`() {
        val emptyList: MutableList<Header> = mutableListOf()

        `when`(configBuilder.logDirectory).thenReturn(null)
        `when`(configBuilder.debugPrintInfo).thenReturn(false)
        `when`(configBuilder.debugPrintHeaders).thenReturn(false)
        `when`(configBuilder.debugPrintTimes).thenReturn(false)
        `when`(configBuilder.enableCache).thenReturn(false)
        `when`(configBuilder.enableResponseCaching).thenReturn(false)
        `when`(configBuilder.responseCacheTimeout).thenReturn(0)
        `when`(configBuilder.connectTimeout).thenReturn(0)
        `when`(configBuilder.readTimeout).thenReturn(0)
        `when`(configBuilder.writeTimeout).thenReturn(0)
        `when`(configBuilder.enableTempCookieJar).thenReturn(false)
        `when`(configBuilder.headers).thenReturn(emptyList)
        `when`(configBuilder.cookieJar).thenReturn(null)
        `when`(configBuilder.hostnameVerifier).thenReturn(null)
        `when`(configBuilder.sslSocketFactory).thenReturn(null)
        `when`(configBuilder.trustAllCerts).thenReturn(false)

        restClientConfig = RestClientConfig(configBuilder)

        Assertions.assertNull(restClientConfig.logDirectory)
        assertFalse(restClientConfig.debugPrintInfo)
        assertFalse(restClientConfig.debugPrintHeaders)
        assertFalse(restClientConfig.debugPrintTimes)
        assertFalse(restClientConfig.enableCache)
        assertFalse(restClientConfig.enableResponseCaching)
        assertEquals(0, restClientConfig.responseCacheTimeout)
        assertEquals(0, restClientConfig.connectTimeout)
        assertEquals(0, restClientConfig.readTimeout)
        assertEquals(0, restClientConfig.writeTimeout)
        assertFalse(restClientConfig.enableTempCookieJar)
        Assertions.assertNotNull(restClientConfig.headers)
        Assertions.assertNull(restClientConfig.cookieJar)
        Assertions.assertNull(restClientConfig.hostnameVerifier)
        Assertions.assertNull(restClientConfig.sslSocketFactory)
        assertFalse(restClientConfig.trustAllCerts)
    }

    @Test
    fun `test initialization with custom values`() {
        val customHeaders = mutableListOf(mockHeader)
        val customFile = File("custom/log/directory")
        val customCookieJar = mock(CookieJar::class.java)
        val customHostnameVerifier = mock(HostnameVerifier::class.java)
        val customSslSocketFactory = mock(SSLSocketFactory::class.java)

        `when`(configBuilder.logDirectory).thenReturn(customFile)
        `when`(configBuilder.debugPrintInfo).thenReturn(true)
        `when`(configBuilder.debugPrintHeaders).thenReturn(true)
        `when`(configBuilder.debugPrintTimes).thenReturn(true)
        `when`(configBuilder.enableCache).thenReturn(true)
        `when`(configBuilder.enableResponseCaching).thenReturn(true)
        `when`(configBuilder.responseCacheTimeout).thenReturn(3600)
        `when`(configBuilder.connectTimeout).thenReturn(30)
        `when`(configBuilder.readTimeout).thenReturn(30)
        `when`(configBuilder.writeTimeout).thenReturn(30)
        `when`(configBuilder.enableTempCookieJar).thenReturn(true)
        `when`(configBuilder.headers).thenReturn(customHeaders)
        `when`(configBuilder.cookieJar).thenReturn(customCookieJar)
        `when`(configBuilder.hostnameVerifier).thenReturn(customHostnameVerifier)
        `when`(configBuilder.sslSocketFactory).thenReturn(customSslSocketFactory)
        `when`(configBuilder.trustAllCerts).thenReturn(true)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customFile, restClientConfig.logDirectory)
        assertTrue(restClientConfig.debugPrintInfo)
        assertTrue(restClientConfig.debugPrintHeaders)
        assertTrue(restClientConfig.debugPrintTimes)
        assertTrue(restClientConfig.enableCache)
        assertTrue(restClientConfig.enableResponseCaching)
        assertEquals(3600, restClientConfig.responseCacheTimeout)
        assertEquals(30, restClientConfig.connectTimeout)
        assertEquals(30, restClientConfig.readTimeout)
        assertEquals(30, restClientConfig.writeTimeout)
        assertTrue(restClientConfig.enableTempCookieJar)
        assertEquals(customHeaders, restClientConfig.headers)
        assertEquals(customCookieJar, restClientConfig.cookieJar)
        assertEquals(customHostnameVerifier, restClientConfig.hostnameVerifier)
        assertEquals(customSslSocketFactory, restClientConfig.sslSocketFactory)
        assertTrue(restClientConfig.trustAllCerts)
    }

    @Test
    fun `test headers are correctly passed`() {
        val customHeaders = mutableListOf(mockHeader)
        `when`(configBuilder.headers).thenReturn(customHeaders)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customHeaders, restClientConfig.headers)
    }

    @Test
    fun `test logDirectory is set correctly`() {
        val customFile = mock(File::class.java)
        `when`(configBuilder.logDirectory).thenReturn(customFile)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customFile, restClientConfig.logDirectory)
    }

    @Test
    fun `test cookieJar is set correctly`() {
        val customCookieJar = mock(CookieJar::class.java)
        `when`(configBuilder.cookieJar).thenReturn(customCookieJar)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customCookieJar, restClientConfig.cookieJar)
    }

    @Test
    fun `test hostnameVerifier is set correctly`() {
        val customHostnameVerifier = mock(HostnameVerifier::class.java)
        `when`(configBuilder.hostnameVerifier).thenReturn(customHostnameVerifier)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customHostnameVerifier, restClientConfig.hostnameVerifier)
    }

    @Test
    fun `test sslSocketFactory is set correctly`() {
        val customSslSocketFactory = mock(SSLSocketFactory::class.java)
        `when`(configBuilder.sslSocketFactory).thenReturn(customSslSocketFactory)

        restClientConfig = RestClientConfig(configBuilder)

        assertEquals(customSslSocketFactory, restClientConfig.sslSocketFactory)
    }

    @Test
    fun `test trustAllCerts is set correctly`() {
        `when`(configBuilder.trustAllCerts).thenReturn(true)

        restClientConfig = RestClientConfig(configBuilder)

        assertTrue(restClientConfig.trustAllCerts)
    }
}
