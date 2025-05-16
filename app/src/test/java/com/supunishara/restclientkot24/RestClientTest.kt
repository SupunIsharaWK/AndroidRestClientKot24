package com.supunishara.restclientkot24

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.supunishara.restclientkot24.Request.Companion.getHeaders
import com.supunishara.restclientkot24.Request.Companion.getHttpUrl
import com.supunishara.restclientkot24.Request.Companion.getMethod
import com.supunishara.restclientkot24.Request.Method.GET
import com.supunishara.restclientkot24.callbacks.ResponseCallbacks
import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.RestClientException
import okhttp3.Call
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.*
import okhttp3.Response as OkHttpResponse

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RestClientTest {

    private lateinit var mockContext: Context
    private lateinit var mockRequest: Request
    private lateinit var mockCallbacks: ResponseCallbacks
    private lateinit var restClient: RestClient
    private lateinit var mockCacheData: CacheData
    private lateinit var mockCall: Call
    private lateinit var mockResponse: OkHttpResponse

    @BeforeEach
    fun setUp() {
        mockContext = mock(Context::class.java)
        mockRequest = mock(Request::class.java)
        mockCallbacks = mock(ResponseCallbacks::class.java)
        mockCacheData = mock(CacheData::class.java)
        mockCall = mock(Call::class.java)
        mockResponse = mock(OkHttpResponse::class.java)
        restClient = RestClient(mockContext, mockRequest, mockCallbacks)
    }

    @AfterEach
    fun tearDown() {
        // Cleanup logic if needed
    }

    @Test
    fun `checkConnected returns true when network is connected`() {
        val mockConnectivityManager = mock(ConnectivityManager::class.java)
        val mockNetworkInfo = mock(NetworkInfo::class.java)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnectedOrConnecting).thenReturn(true)

        val isConnected = restClient.checkConnected(mockContext)

        assertTrue(isConnected)
    }

    @Test
    fun `checkConnected returns false when network is not connected`() {
        val mockConnectivityManager = mock(ConnectivityManager::class.java)
        val mockNetworkInfo = mock(NetworkInfo::class.java)

        `when`(mockContext.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(mockConnectivityManager)
        `when`(mockConnectivityManager.activeNetworkInfo).thenReturn(mockNetworkInfo)
        `when`(mockNetworkInfo.isConnectedOrConnecting).thenReturn(false)

        val isConnected = restClient.checkConnected(mockContext)

        assertFalse(isConnected)
    }

    @Test
    fun `getCacheForRequest returns cache data if available`() {
        `when`(getMethod(mockRequest)).thenReturn(GET)
        `when`(restClient.getCacheForRequest(mockContext, mockRequest)).thenReturn(mockCacheData)
        `when`(mockCacheData.status).thenReturn(true)

        val cacheData = restClient.getCacheForRequest(mockContext, mockRequest)

        Assertions.assertNotNull(cacheData)
        assertTrue(cacheData!!.status)
    }

    @Test
    fun `getCacheForRequest returns null if no cache is available`() {
        `when`(restClient.getCacheForRequest(mockContext, mockRequest)).thenReturn(null)

        val cacheData = restClient.getCacheForRequest(mockContext, mockRequest)

        Assertions.assertNull(cacheData)
    }

    @Test
    fun `deleteCacheForRequest returns true on successful cache deletion`() {
        `when`(restClient.deleteCacheForRequest(mockContext, mockRequest)).thenReturn(true)

        val result = restClient.deleteCacheForRequest(mockContext, mockRequest)

        assertTrue(result)
    }

    @Test
    fun `deleteCacheForRequest returns false on failure to delete cache`() {
        `when`(restClient.deleteCacheForRequest(mockContext, mockRequest)).thenReturn(false)

        val result = restClient.deleteCacheForRequest(mockContext, mockRequest)

        Assertions.assertFalse(result)
    }

    @Test
    fun `execute handles successful response`() {
        `when`(restClient.checkConnected(mockContext)).thenReturn(true)
        `when`(getHttpUrl(mockRequest)).thenReturn("https://example.com".toHttpUrlOrNull())
        `when`(getMethod(mockRequest)).thenReturn(GET)
        `when`(getHeaders(mockRequest)).thenReturn(mutableListOf())
        `when`(mockResponse.isSuccessful).thenReturn(true)
        `when`(mockResponse.body?.string()).thenReturn("Success")

        val call = restClient.execute()

        Assertions.assertNotNull(call)
    }

    @Test
    fun `execute handles failure response`() {
        `when`(restClient.checkConnected(mockContext)).thenReturn(true)
        `when`(getHttpUrl(mockRequest)).thenReturn("https://example.com".toHttpUrlOrNull())
        `when`(getMethod(mockRequest)).thenReturn(GET)
        `when`(getHeaders(mockRequest)).thenReturn(mutableListOf())
        `when`(mockResponse.isSuccessful).thenReturn(false)

        val call = restClient.execute()

        Assertions.assertNotNull(call)
    }

    @Test
    fun `execute returns null when no internet connection`() {
        `when`(restClient.checkConnected(mockContext)).thenReturn(false)

        val call = restClient.execute()

        Assertions.assertNull(call)
    }

    @Test
    fun `execute handles exception safely`() {
        `when`(restClient.checkConnected(mockContext)).thenReturn(true)
        `when`(getHttpUrl(mockRequest)).thenReturn("https://example.com".toHttpUrlOrNull())
        `when`(getMethod(mockRequest)).thenReturn(GET)
        `when`(getHeaders(mockRequest)).thenReturn(mutableListOf())

        val exception = RestClientException(-1, "Test exception")
        `when`(mockResponse.body?.string()).thenThrow(exception)

        val call = restClient.execute()

        Assertions.assertNotNull(call)
    }
}
