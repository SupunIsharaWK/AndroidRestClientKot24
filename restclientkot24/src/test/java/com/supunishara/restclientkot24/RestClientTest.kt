package com.supunishara.restclientkot24

import okhttp3.Response
import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.supunishara.restclientkot24.callbacks.MultiResponseCallback
import com.supunishara.restclientkot24.data_classes.Header
import com.supunishara.restclientkot24.configs.ConfigBuilder
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.io.IOException

class RestClientTest {

    private lateinit var client: OkHttpClient
    private lateinit var call: Call
    private lateinit var callback: MultiResponseCallback
    private lateinit var request: com.supunishara.restclientkot24.Request
    private lateinit var restClient: com.supunishara.restclientkot24.RestClient
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        client = mock(OkHttpClient::class.java)
        call = mock(Call::class.java)
        callback = mock(MultiResponseCallback::class.java)

        val config = ConfigBuilder()
            .setConnectTimeout(5)
            .setReadTimeout(5)
            .setWriteTimeout(5)
            .build()

        restClient = com.supunishara.restclientkot24.RestClient(context)
        restClient.setConfig(config)
        restClient.client = client

        request = com.supunishara.restclientkot24.Request.fromUrl(
            "https://example.com",
            com.supunishara.restclientkot24.Request.Method.GET
        )
        request.addHeader(Header("Accept", "application/json"))
    }

//    @Test
//    fun testExecuteRequestSuccess() {
//        val responseBody =
//         """{"status":"ok"}""".toResponseBody("application/json".toMediaTypeOrNull())
//        val mockRequest = okhttp3.Request.Builder().url("https://example.com").build()
//        val okResponse = Response.Builder()
//            .code(200)
//            .protocol(Protocol.HTTP_1_1)
//            .message("OK")
//            .request(mockRequest)
//            .body(responseBody)
//            .build()
//
//        `when`(client.newCall(any())).thenReturn(call)
//        doAnswer {
//            val callbackCaptor = it.getArgument<Callback>(0)
//            callbackCaptor.onResponse(call, okResponse)
//            null
//        }.`when`(call).enqueue(any())
//
//        restClient.executeRequest(request, callback)
//
//        verify(callback).onSuccess(any())
//        verify(callback, never()).onFailure(any())
//    }

//    @Test
//    fun testExecuteRequestFailure() {
//        val exception = IOException("Network failure")
//
//        `when`(client.newCall(any())).thenReturn(call)
//        doAnswer {
//            val callbackCaptor = it.getArgument<Callback>(0)
//            callbackCaptor.onFailure(call, exception)
//            null
//        }.`when`(call).enqueue(any())
//
//        restClient.executeRequest(request, callback)
//
//        verify(callback).onFailure(any())
//        verify(callback, never()).onSuccess(any())
//    }

//    @Test
//    fun testExecuteRequestSuspendSuccess() = runBlocking {
//        val responseBody =
//         """{"status":"ok"}""".toResponseBody("application/json".toMediaTypeOrNull())
//        val mockRequest = okhttp3.Request.Builder().url("https://example.com").build()
//        val okResponse = Response.Builder()
//            .code(200)
//            .protocol(Protocol.HTTP_1_1)
//            .message("OK")
//            .request(mockRequest)
//            .body(responseBody)
//            .build()
//
//        `when`(client.newCall(any())).thenReturn(call)
//        doAnswer {
//            val callbackCaptor = it.getArgument<Callback>(0)
//            callbackCaptor.onResponse(call, okResponse)
//            null
//        }.`when`(call).enqueue(any())
//
//        val response = restClient.executeRequestSuspend(request)
//
//        assertEquals(200, response.statusCode)
//        assertTrue(response.isSuccessful)
//        assertEquals("{\"status\":\"ok\"}", response.responseBody)
//    }

//    @Test
//    fun testExecuteRequestSuspendFailure() = runBlocking {
//        val exception = IOException("Timeout")
//
//        `when`(client.newCall(any())).thenReturn(call)
//        doAnswer {
//            val callbackCaptor = it.getArgument<Callback>(0)
//            callbackCaptor.onFailure(call, exception)
//            null
//        }.`when`(call).enqueue(any())
//
//        val response = restClient.executeRequestSuspend(request)
//
//        assertTrue(response.isException)
//        assertEquals("java.io.IOException: Timeout", response.statusMessage)
//    }
}
