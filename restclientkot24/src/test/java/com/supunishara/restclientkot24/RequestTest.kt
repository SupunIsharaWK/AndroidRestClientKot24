package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.FormBody
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RequestTest {

    private lateinit var request: com.supunishara.restclientkot24.Request

    @Before
    fun setUp() {
        request = com.supunishara.restclientkot24.Request.fromUrl("https://api.example.com", com.supunishara.restclientkot24.Request.Method.POST)
    }

    @Test
    fun testDefaultState() {
        assertEquals(com.supunishara.restclientkot24.Request.Method.POST, request.method)
        assertEquals(com.supunishara.restclientkot24.Request.BodyType.EMPTY, request.bodyType)
        assertTrue(request.getHeaders().isEmpty())
        assertEquals(0, request.connectTimeout)
        assertEquals(0, request.readTimeout)
        assertEquals(0, request.writeTimeout)
        assertEquals(false, request.trustAllCerts)
    }

    @Test
    fun testSetTimeouts() {
        request.setTimeouts(3000)
        assertEquals(3000, request.connectTimeout)
        assertEquals(3000, request.readTimeout)
        assertEquals(3000, request.writeTimeout)
    }

    @Test
    fun testEnableDisableCaching() {
        request.enableResponseCaching(1000)
        assertEquals(1, request.responseCachingStatus)
        assertEquals(1000, request.responseCacheTimeout)

        request.disableResponseCaching()
        assertEquals(2, request.responseCachingStatus)
    }

    @Test
    fun testHeaderAddReplaceRemoveClear() {
        val header = Header("Accept", "application/json")
        request.addHeader(header)
        assertEquals(1, request.getHeaders().size)

        val newHeader = Header("Accept", "application/xml")
        request.addHeader(newHeader, replace = true)
        assertEquals("application/xml", request.getHeaders()[0].value)

        request.removeHeader("Accept")
        assertTrue(request.getHeaders().isEmpty())

        request.addHeader(header)
        request.clearHeaders()
        assertTrue(request.getHeaders().isEmpty())
    }

    @Test
    fun testSetRawTextBody() {
        request.setBody("hello world", com.supunishara.restclientkot24.Request.BodyType.TEXT)
        assertEquals("hello world", request.rawBody)
        assertEquals(com.supunishara.restclientkot24.Request.BodyType.TEXT, request.bodyType)
        assertTrue(
            request.getHeaders()
                .any { it.name.equals("Content-Type", true) && it.value.contains("text/plain") })
    }

//    @Test
//    fun testSetJsonObjectBody() {
//        val json = JSONObject()
//        json.put("name", "chatgpt")
//        request.setBody(json)
//        assertEquals(com.supunishara.restclientkot24.Request.BodyType.JSON, request.bodyType)
//        assertTrue(request.rawBody!!.contains("chatgpt"))
//    }
//
//    @Test
//    fun testSetJsonArrayBody() {
//        val jsonArray = JSONArray()
//            jsonArray.put("item1")
//        request.setBody(jsonArray)
//        assertEquals(com.supunishara.restclientkot24.Request.BodyType.JSON, request.bodyType)
//        assertTrue(request.rawBody!!.contains("item1"))
//    }

    @Test
    fun testSetFormBody() {
        val form = FormBody.Builder().add("key", "value").build()
        request.setBody(form)
        assertEquals(com.supunishara.restclientkot24.Request.BodyType.X_FORM_URL_ENCODED, request.bodyType)
        assertNotNull(request.formBody)
    }

//    @Test
//    fun testSetMultipartBody() {
//        val multipart = MultipartBody.Builder().setType(MultipartBody.FORM).build()
//        request.setBody(multipart)
//        assertEquals(com.supunishara.restclientkot24.Request.BodyType.MULTIPART, request.bodyType)
//    }

    @Test(expected = IllegalArgumentException::class)
    fun testFromUrlFailsOnInvalidUrl() {
        com.supunishara.restclientkot24.Request.fromUrl("not-a-url", com.supunishara.restclientkot24.Request.Method.GET)
    }
}
