package com.supunishara.restclientkot24

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.supunishara.restclientkot24.callbacks.MultiResponseCallback
import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.Header
import com.supunishara.restclientkot24.exceptions.ConnectionException
import com.supunishara.restclientkot24.utils.NetworkUtil
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request as OkHttpRequest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import io.mockk.every
import io.mockk.mockkObject
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

/**
 * Unit tests for RestClient using mocked OkHttp and network utilities.
 * Covers success, failure, and network connection scenarios.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28], manifest = Config.NONE)
class RestClientTest {

    private lateinit var client: OkHttpClient
    private lateinit var call: Call
    private lateinit var callback: MultiResponseCallback
    private lateinit var request: Request
    private lateinit var restClient: RestClient
    private lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
        client = mock()
        call = mock()
        callback = mock()

        val config = ConfigBuilder()
            .setConnectTimeout(5)
            .setReadTimeout(5)
            .setWriteTimeout(5)
            .build()

        restClient = RestClient(context)
        restClient.setConfig(config)
        restClient.client = client

        request = Request.fromUrl("https://example.com", Request.Method.GET)
        request.addHeader(Header("Accept", "application/json"))

        mockkObject(NetworkUtil)
        every { NetworkUtil.isConnected(any()) } returns true // Default mock
    }

    @Test
    fun testExecuteRequestSuccess() {
        every { NetworkUtil.isConnected(eq(context)) } returns true

        val responseBody = """{"status":"200"}""".toResponseBody("application/json".toMediaTypeOrNull())
        val okHttpRequest = OkHttpRequest.Builder().url("https://example.com").build()
        val okhttpResponse = okhttp3.Response.Builder()
            .request(okHttpRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        whenever(client.newCall(any())).thenReturn(call)
        doAnswer {
            val cb = it.getArgument<Callback>(0)
            cb.onResponse(call, okhttpResponse)
            null
        }.whenever(call).enqueue(any())

        restClient.executeRequest(request, callback)

        verify(callback).onSuccess(any())
        verify(callback, never()).onFailure(any())
    }

    @Test
    fun testExecuteRequestFailure() {
        every { NetworkUtil.isConnected(any()) } returns true
        val exception = IOException("Network failure")

        whenever(client.newCall(any())).thenReturn(call)
        doAnswer {
            val cb = it.getArgument<Callback>(0)
            cb.onFailure(call, exception)
            null
        }.whenever(call).enqueue(any())

        restClient.executeRequest(request, callback)

        verify(callback).onFailure(any())
        verify(callback, never()).onSuccess(any())
    }

    @Test
    fun testExecuteRequestSuspendSuccess() = runBlocking {
        every { NetworkUtil.isConnected(eq(context)) } returns true

        val responseBody = """{"status":"ok"}"""

        whenever(client.newCall(any())).thenReturn(call)
        doAnswer {
            val cb = it.getArgument<Callback>(0)
            val okhttpResponse = okhttp3.Response.Builder()
                .request(OkHttpRequest.Builder().url("https://example.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(responseBody.toResponseBody("application/json".toMediaTypeOrNull()))
                .build()
            cb.onResponse(call, okhttpResponse)
            null
        }.whenever(call).enqueue(any())

        val response = restClient.executeRequestSuspend(request)

         assertEquals(200, response.statusCode)
        assertTrue(response.isSuccessful)
        assertEquals(responseBody, response.responseBody)
    }

    @Test
    fun testExecuteRequestSuspendFailure() = runBlocking {
        every { NetworkUtil.isConnected(any()) } returns true
        val exception = IOException("Timeout")

        whenever(client.newCall(any())).thenReturn(call)
        doAnswer {
            val cb = it.getArgument<Callback>(0)
            cb.onFailure(call, exception)
            null
        }.whenever(call).enqueue(any())

        val response = restClient.executeRequestSuspend(request)

        assertTrue(response.isException)
        assertEquals("java.io.IOException: Timeout", response.statusMessage)
    }

    @Test
    fun testConnectionFailureTriggersOnFailureCallback() {
        restClient.client = null
        every { NetworkUtil.isConnected(any()) } returns true

        restClient.executeRequest(request, callback)

        verify(callback).onFailure(any<ConnectionException>())
        verify(callback, never()).onSuccess(any())
    }

    @Test
    fun testNoInternetConnectionMocked() {
        every { NetworkUtil.isConnected(any()) } returns false

        restClient.executeRequest(request, callback)

        verify(callback).onFailure(any<ConnectionException>())
        verify(callback, never()).onSuccess(any())
    }

    @Test
    fun testInternetConnectionAvailable() {
        every { NetworkUtil.isConnected(any()) } returns true

        val responseBody = """{"status":"200"}""".toResponseBody("application/json".toMediaTypeOrNull())
        val okHttpRequest = OkHttpRequest.Builder().url("https://example.com").build()
        val okhttpResponse = okhttp3.Response.Builder()
            .request(okHttpRequest)
            .protocol(Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .body(responseBody)
            .build()

        whenever(client.newCall(any())).thenReturn(call)
        doAnswer {
            val cb = it.getArgument<Callback>(0)
            cb.onResponse(call, okhttpResponse)
            null
        }.whenever(call).enqueue(any())

        restClient.executeRequest(request, callback)

        verify(callback).onSuccess(any())
        verify(callback, never()).onFailure(any())
    }
}
