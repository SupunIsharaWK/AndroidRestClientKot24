package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.RestClientException
import okhttp3.Headers
import okhttp3.Protocol
import okhttp3.Response as OkHttpResponse
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ResponseTest {

 private lateinit var response: com.supunishara.restclientkot24.Response

 @Before
 fun setUp() {
  response = com.supunishara.restclientkot24.Response()
 }

 @Test
 fun testDefaultValues() {
  assertFalse(response.isSuccessful)
  assertFalse(response.isExecuted)
  assertFalse(response.isException)
  assertFalse(response.isCache)
  assertEquals("", response.responseBody)
  assertEquals(0, response.statusCode)
  assertEquals("Unknown response", response.statusMessage)
 }

 @Test
 fun testWithException() {
  val exception = RestClientException(-1, "Simulated failure")
  response.exception = exception

  assertTrue(response.isException)
  assertEquals(-1, response.statusCode)
  assertEquals("com.supunishara.restclientkot24.exceptions.RestClientException: Simulated failure", response.statusMessage)
 }

 @Test
 fun testWithHttpResponse() {
  val okhttpResponse = OkHttpResponse.Builder()
   .code(200)
   .protocol(Protocol.HTTP_1_1)
   .message("OK")
   .request(okhttp3.Request.Builder().url("https://example.com").build())
   .build()

  response.httpResponse = okhttpResponse

  assertTrue(response.isSuccessful)
  assertEquals(200, response.statusCode)
  assertEquals("OK", response.statusMessage)
 }

 @Test
 fun testHeadersAccess() {
  val okhttpResponse = OkHttpResponse.Builder()
   .code(200)
   .protocol(Protocol.HTTP_1_1)
   .message("OK")
   .headers(Headers.headersOf("X-Test", "123", "X-Test", "456"))
   .request(okhttp3.Request.Builder().url("https://example.com").build())
   .build()

  response.httpResponse = okhttpResponse
  val headers = response.headers

  assertNotNull(headers)
  assertTrue(headers!!.containsKey("X-Test"))
  assertEquals(2, headers["X-Test"]?.size)
 }

 @Test
 fun testCacheDataIntegration() {
  val cache = CacheData(
   url = "https://example.com",
   method = "get",
   data = "{\"cached\":true}",
   createdAt = System.currentTimeMillis(),
   timeout = 5000
  )
  response.cacheData = cache

  assertTrue(response.isCache)
  assertTrue(response.isSuccessful)
  assertEquals("{\"cached\":true}", response.cacheData!!.data)
 }

 @Test
 fun testToStringReturnsBody() {
  response.responseBody = "This is a body"
  assertEquals("This is a body", response.toString())
 }
}
