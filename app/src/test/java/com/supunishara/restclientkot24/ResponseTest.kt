package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.RestClientException
import okhttp3.Call
import okhttp3.Response as OkHttpResponse
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResponseTest {

    private lateinit var mockRequest: Request
    private lateinit var mockResponse: OkHttpResponse
    private lateinit var mockCall: Call
    private lateinit var mockException: RestClientException
    private lateinit var mockCacheData: CacheData
    private lateinit var response: Response

    @BeforeEach
    fun setup() {
        mockRequest = mock(Request::class.java)
        mockResponse = mock(OkHttpResponse::class.java)
        mockCall = mock(Call::class.java)
        mockException = mock(RestClientException::class.java)
        mockCacheData = mock(CacheData::class.java)
    }

    @Test
    fun `Response constructor with valid arguments sets correct values`() {
        `when`(mockResponse.code).thenReturn(200)
        `when`(mockResponse.message).thenReturn("OK")
        `when`(mockCacheData.data).thenReturn("Cache Data")

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            mockCacheData
        )

        assertEquals(200, response.getStatusCode())
        assertEquals("OK", response.getStatusMessage())
        Assertions.assertNotNull(response.getHeaders())
        assertTrue(response.isCache())
    }

    @Test
    fun `Response constructor with exception sets status correctly`() {
        `when`(mockException.getErrorCode()).thenReturn(500)

        response = Response(
            null,
            null,
            null,
            null,
            mockException,
            null
        )

        assertEquals(500, response.getStatusCode())
        assertEquals(mockException.toString(), response.getStatusMessage())
    }

    @Test
    fun `getHeaders returns valid headers map`() {
        val headersMap = mapOf("Content-Type" to listOf("application/json"))
        `when`(mockResponse.headers.toMultimap()).thenReturn(headersMap)

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            null
        )

        val headers = response.getHeaders()
        Assertions.assertNotNull(headers)
        assertTrue(headers?.containsKey("Content-Type") == true)
    }

    @Test
    fun `getHeaders returns null on exception`() {
        `when`(mockResponse.headers.toMultimap()).thenThrow(Exception("Header retrieval failed"))

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            null
        )

        val headers = response.getHeaders()
        Assertions.assertNull(headers)
    }

    @Test
    fun `isSuccessful returns true for successful HTTP response`() {
        `when`(mockResponse.isSuccessful).thenReturn(true)

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            null
        )

        assertTrue(response.isSuccessful())
    }

    @Test
    fun `isSuccessful returns true for cache data`() {
        response = Response(
            mockRequest,
            "Response Body",
            null,
            null,
            null,
            mockCacheData
        )

        assertTrue(response.isSuccessful())
    }

    @Test
    fun `isExecuted returns true for executed call`() {
        `when`(mockCall.isExecuted()).thenReturn(true)

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            null
        )

        assertTrue(response.isExecuted())
    }

    @Test
    fun `isExecuted returns false for non-executed call`() {
        `when`(mockCall.isExecuted()).thenReturn(false)

        response = Response(
            mockRequest,
            "Response Body",
            mockResponse,
            mockCall,
            null,
            null
        )

        assertFalse(response.isExecuted())
    }

    @Test
    fun `isException returns true when exception is set`() {
        response = Response(
            mockRequest,
            "Response Body",
            null,
            null,
            mockException,
            null
        )

        assertTrue(response.isException())
    }

    @Test
    fun `isCache returns false when cache data is null`() {
        response = Response(
            mockRequest,
            "Response Body",
            null,
            null,
            null,
            null
        )

        assertFalse(response.isCache())
    }
}