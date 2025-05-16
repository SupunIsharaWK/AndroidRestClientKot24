package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import io.mockk.mockk
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RequestTest {

    private lateinit var request: Request
    private lateinit var url: HttpUrl

    @BeforeEach
    fun setUp() {
        url = "https://example.com".toHttpUrlOrNull()!!
        request = Request(url, Request.Method.POST)
    }

    @AfterEach
    fun tearDown() {
        // Any cleanup if needed
    }

    @Test
    fun `default bodyType is EMPTY`() {
        assertEquals(Request.BodyType.EMPTY, Request.getBodyType(request))
    }

    @Test
    fun `setRawBody with JSON sets correct Content-Type`() {
        val json = """{"key":"value"}"""
        Request.setRawBody(request, json, Request.BodyType.JSON)

        val headers = Request.getHeaders(request)
        assertTrue(headers!!.any { it.name == "Content-Type" && it.value.contains("application/json") })
        assertEquals(json, Request.getRawBody(request))
    }

    @Test
    fun `setJSONObject sets correct rawBody and header`() {
        val jsonObject = JSONObject().apply { put("name", "Supun") }
        Request.setJSONObject(request, jsonObject)

        assertEquals(jsonObject.toString(), Request.getRawBody(request))
        assertTrue(Request.getHeaders(request)!!.any { it.value.contains("application/json") })
    }

    @Test
    fun `enableResponseCaching sets status and timeout`() {
        Request.enableResponseCaching(request, 30)
        assertEquals(30, Request.getResponseCacheTimeout(request))
        assertEquals(1, Request.getResponseCachingStatus(request))
    }

    @Test
    fun `setFormBody sets body and header`() {
        val formBody = FormBody.Builder()
            .add("username", "admin")
            .add("password", "1234")
            .build()

        Request.setFormBody(request, formBody)

        assertEquals(formBody, Request.getFormBody(request))
        assertEquals(Request.BodyType.X_FORM_URL_ENCODED, Request.getBodyType(request))
        assertTrue(Request.getHeaders(request)!!
            .any { it.value.contains("application/x-www-form-urlencoded") })
    }

    @Test
    fun `addHeader adds header correctly`() {
        val header = Header("X-Test", "Unit")
        Request.addHeader(request, header)

        val headers = Request.getHeaders(request)
        assertTrue(headers!!.contains(header))
    }

    @Test
    fun `setPlainText sets rawBody and Content-Type`() {
        Request.setPlainText(request, "Hello World")
        assertEquals("Hello World", Request.getRawBody(request))
        assertEquals(Request.BodyType.TEXT, Request.getBodyType(request))
        assertTrue(Request.getHeaders(request)!!.any { it.value.contains("text/plain") })
    }

    @Test
    fun `forceGzipDecode sets flag true`() {
        Request.forceGzipDecode(request)
        assertTrue(Request.isForceGzipDecode(request))
    }

    @Test
    fun `setMultipartBody sets body and Content-Type header`() {
        val multipartBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("field1", "value1")
            .build()

        Request.setMultipartBody(request, multipartBody)

        assertEquals(multipartBody, Request.getMultipartBody(request))
        assertEquals(Request.BodyType.MULTIPART, Request.getBodyType(request))
        assertTrue(Request.getHeaders(request)!!.any { it.value.contains("multipart/form-data") })
    }

    @Test
    fun `setJSONArray sets rawBody and correct Content-Type`() {
        val jsonArray = JSONArray()
        jsonArray.put(JSONObject().put("id", 1))
        jsonArray.put(JSONObject().put("id", 2))

        Request.setJSONArray(request, jsonArray)

        assertEquals(jsonArray.toString(), Request.getRawBody(request))
        assertEquals(Request.BodyType.JSON, Request.getBodyType(request))
        assertTrue(Request.getHeaders(request)!!.any { it.value.contains("application/json") })
    }

    @Test
    fun `set connection and read timeouts`() {
        Request.setConnectTimeout(request, 15)
        Request.setReadTimeout(request, 20)

        assertEquals(15, Request.setConnectTimeout(request, 15))
        assertEquals(20, Request.getReadTimeout(request))
    }

    @Test
    fun `disableGzipDecode sets flag to false`() {
        Request.disableGzipDecode(request)
        assertFalse(Request.isForceGzipDecode(request))
    }

    @Test
    fun `set and get hostname verifier`() {
        val customVerifier = HostnameVerifier { _, _ -> true }
        Request.setHostnameVerifier(request, customVerifier)

        assertEquals(customVerifier, Request.getHostnameVerifier(request))
    }

    @Test
    fun `set and get SSL socket factory`() {
        val mockFactory = mockk<SSLSocketFactory>()
        Request.setSslSocketFactory(request, mockFactory)

        assertEquals(mockFactory, Request.getSslSocketFactory(request))
    }

    @Test
    fun `multiple headers are preserved`() {
        Request.addHeader(request, Header("Header1", "Value1"))
        Request.addHeader(request, Header("Header2", "Value2"))

        val headers = Request.getHeaders(request)
        assertEquals(2, headers!!.size)
        assertTrue(headers.any { it.name == "Header1" && it.value == "Value1" })
        assertTrue(headers.any { it.name == "Header2" && it.value == "Value2" })
    }

    @Test
    fun `raw body can exist without content type header`() {
        Request.setRawBody(request, "Hello raw body", Request.BodyType.EMPTY)

        val headers = Request.getHeaders(request)
        assertEquals("Hello raw body", Request.getRawBody(request))
        assertEquals(Request.BodyType.EMPTY, Request.getBodyType(request))
        assertTrue(headers == null || headers.none { it.name == "Content-Type" })
    }

    @Test
    fun `all BodyType enum values exist`() {
        val expected = listOf(
            Request.BodyType.EMPTY,
            Request.BodyType.JSON,
            Request.BodyType.X_FORM_URL_ENCODED,
            Request.BodyType.MULTIPART,
            Request.BodyType.TEXT,
            Request.BodyType.XML
        )
        assertEquals(expected.size, Request.BodyType.entries.size)
        assertTrue(Request.BodyType.entries.containsAll(expected))
    }
}
