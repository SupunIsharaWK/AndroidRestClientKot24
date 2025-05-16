package com.supunishara.restclientkot24.configs

import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

class ConfigBuilderTest {

    private lateinit var configBuilder: ConfigBuilder
    private lateinit var mockFile: File
    private lateinit var mockCookieJar: CookieJar
    private lateinit var mockHostnameVerifier: HostnameVerifier
    private lateinit var mockSslSocketFactory: SSLSocketFactory
    private lateinit var mockHeader: Header

    @BeforeEach
    fun setUp() {
        configBuilder = ConfigBuilder()
        mockFile = mock(File::class.java)
        mockCookieJar = mock(CookieJar::class.java)
        mockHostnameVerifier = mock(HostnameVerifier::class.java)
        mockSslSocketFactory = mock(SSLSocketFactory::class.java)
        mockHeader = Header("Content-Type", "application/json")
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun getLogDirectory() {
    }

    @Test
    fun setLogDirectory() {
    }

    @Test
    fun getDebugPrintInfo() {
    }

    @Test
    fun setDebugPrintInfo() {
    }

    @Test
    fun getDebugPrintHeaders() {
    }

    @Test
    fun setDebugPrintHeaders() {
    }

    @Test
    fun getDebugPrintTimes() {
    }

    @Test
    fun setDebugPrintTimes() {
    }

    @Test
    fun getEnableCache() {
    }

    @Test
    fun setEnableCache() {
    }

    @Test
    fun getEnableResponseCaching() {
    }

    @Test
    fun setEnableResponseCaching() {
    }

    @Test
    fun getResponseCacheTimeout() {
    }

    @Test
    fun setResponseCacheTimeout() {
    }

    @Test
    fun getTrustAllCerts() {
    }

    @Test
    fun setTrustAllCerts() {
    }

    @Test
    fun getConnectTimeout() {
    }

    @Test
    fun setConnectTimeout() {
    }

    @Test
    fun getReadTimeout() {
    }

    @Test
    fun setReadTimeout() {
    }

    @Test
    fun getWriteTimeout() {
    }

    @Test
    fun setWriteTimeout() {
    }

    @Test
    fun getEnableTempCookieJar() {
    }

    @Test
    fun setEnableTempCookieJar() {
    }

    @Test
    fun getHeaders() {
    }

    @Test
    fun setHeaders() {
    }

    @Test
    fun getCookieJar() {
    }

    @Test
    fun setCookieJar() {
    }

    @Test
    fun getHostnameVerifier() {
    }

    @Test
    fun setHostnameVerifier() {
    }

    @Test
    fun getSslSocketFactory() {
    }

    @Test
    fun setSslSocketFactory() {
    }

    @Test
    fun setDefaultJSONConfig() {
    }

    @Test
    fun addHeader() {
    }

    @Test
    fun debugPrintInfo() {
    }

    @Test
    fun debugPrintHeaders() {
    }

    @Test
    fun debugPrintTimes() {
    }

    @Test
    fun enableFileLogger() {
    }

    @Test
    fun enableCache() {
    }

    @Test
    fun enableResponseCaching() {
    }

    @Test
    fun enableCookieJar() {
    }

    @Test
    fun trustAllCerts() {
    }

    @Test
    fun testSetConnectTimeout() {
    }

    @Test
    fun testSetReadTimeout() {
    }

    @Test
    fun testSetWriteTimeout() {
    }

    @Test
    fun setAllTimeouts() {
    }

    @Test
    fun testSetHostnameVerifier() {
    }

    @Test
    fun setSSLSocketFactory() {
    }

    @Test
    fun enableTempCookieJar() {
    }

    @Test
    fun build() {
    }

    @Test
    fun `test setDefaultJSONConfig adds Accept header`() {
        // Act
        configBuilder.setDefaultJSONConfig()

        // Assert
        val config = configBuilder.build()
        val header = config.headers?.find { it.name == "Accept" }
        assertNotNull(header)
        assertEquals("application/json", header?.value)
    }

    @Test
    fun `test addHeader adds a custom header`() {
        // Act
        configBuilder.addHeader(mockHeader)

        // Assert
        val config = configBuilder.build()
        val header = config.headers?.find { it.name == "Content-Type" }
        assertNotNull(header)
        assertEquals("application/json", header?.value)
    }

    @Test
    fun `test debugPrintInfo enables debugPrintInfo`() {
        // Act
        configBuilder.debugPrintInfo()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.debugPrintInfo)
    }

    @Test
    fun `test debugPrintHeaders enables debugPrintHeaders`() {
        // Act
        configBuilder.debugPrintHeaders()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.debugPrintHeaders)
    }

    @Test
    fun `test debugPrintTimes enables debugPrintTimes`() {
        // Act
        configBuilder.debugPrintTimes()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.debugPrintTimes)
    }

    @Test
    fun `test enableFileLogger sets log directory`() {
        // Act
        configBuilder.enableFileLogger(mockFile)

        // Assert
        val config = configBuilder.build()
        assertEquals(mockFile, config.logDirectory)
    }

    @Test
    fun `test enableCache sets enableCache to true`() {
        // Act
        configBuilder.enableCache()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.enableCache)
    }

    @Test
    fun `test enableResponseCaching sets responseCacheTimeout and enableResponseCaching to true`() {
        // Act
        configBuilder.enableResponseCaching(3600000)

        // Assert
        val config = configBuilder.build()
        assertTrue(config.enableResponseCaching)
        assertEquals(3600000, config.responseCacheTimeout)
    }

    @Test
    fun `test enableCookieJar sets cookieJar`() {
        // Act
        configBuilder.enableCookieJar(mockCookieJar)

        // Assert
        val config = configBuilder.build()
        assertEquals(mockCookieJar, config.cookieJar)
    }

    @Test
    fun `test trustAllCerts sets trustAllCerts to true`() {
        // Act
        configBuilder.trustAllCerts()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.trustAllCerts)
    }

    @Test
    fun `test setConnectTimeout sets connectTimeout`() {
        // Act
        configBuilder.setConnectTimeout(15)

        // Assert
        val config = configBuilder.build()
        assertEquals(15, config.connectTimeout)
    }

    @Test
    fun `test setReadTimeout sets readTimeout`() {
        // Act
        configBuilder.setReadTimeout(15)

        // Assert
        val config = configBuilder.build()
        assertEquals(15, config.readTimeout)
    }

    @Test
    fun `test setWriteTimeout sets writeTimeout`() {
        // Act
        configBuilder.setWriteTimeout(15)

        // Assert
        val config = configBuilder.build()
        assertEquals(15, config.writeTimeout)
    }

    @Test
    fun `test setAllTimeouts sets all timeouts`() {
        // Act
        configBuilder.setAllTimeouts(20)

        // Assert
        val config = configBuilder.build()
        assertEquals(20, config.connectTimeout)
        assertEquals(20, config.readTimeout)
        assertEquals(20, config.writeTimeout)
    }

    @Test
    fun `test setHostnameVerifier sets hostnameVerifier`() {
        // Act
        configBuilder.setHostnameVerifier(mockHostnameVerifier)

        // Assert
        val config = configBuilder.build()
        assertEquals(mockHostnameVerifier, config.hostnameVerifier)
    }

    @Test
    fun `test setSSLSocketFactory sets sslSocketFactory`() {
        // Act
        configBuilder.setSSLSocketFactory(mockSslSocketFactory)

        // Assert
        val config = configBuilder.build()
        assertEquals(mockSslSocketFactory, config.sslSocketFactory)
    }

    @Test
    fun `test enableTempCookieJar sets enableTempCookieJar to true`() {
        // Act
        configBuilder.enableTempCookieJar()

        // Assert
        val config = configBuilder.build()
        assertTrue(config.enableTempCookieJar)
    }
}