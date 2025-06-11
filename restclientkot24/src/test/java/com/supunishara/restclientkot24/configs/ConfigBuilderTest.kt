package com.supunishara.restclientkot24.configs

import com.supunishara.restclientkot24.data_classes.Header
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class ConfigBuilderTest {

 @Test
 fun testDefaultValues() {
  val config = ConfigBuilder().build()

  assertFalse(config.debugPrintInfo)
  assertFalse(config.debugPrintHeaders)
  assertFalse(config.debugPrintTimes)
  assertFalse(config.enableCache)
  assertFalse(config.enableResponseCaching)
  assertEquals(86400000, config.responseCacheTimeout)
  assertFalse(config.trustAllCerts)
  assertEquals(10, config.connectTimeout)
  assertEquals(10, config.readTimeout)
  assertEquals(10, config.writeTimeout)
  assertFalse(config.enableTempCookieJar)
  assertTrue(config.headers.isEmpty())
 }

 @Test
 fun testCustomHeader() {
  val header = Header("Accept", "application/json")
  val config = ConfigBuilder().addHeader(header).build()
  assertTrue(config.headers.contains(header))
 }

 @Test
 fun testDefaultJsonHeader() {
  val config = ConfigBuilder().setDefaultJSONConfig().build()
  assertTrue(config.headers.any { it.name.equals("Accept", true) && it.value == "application/json" })
 }

 @Test
 fun testDebugFlags() {
  val config = ConfigBuilder()
   .debugPrintInfo()
   .debugPrintHeaders()
   .debugPrintTimes()
   .build()

  assertTrue(config.debugPrintInfo)
  assertTrue(config.debugPrintHeaders)
  assertTrue(config.debugPrintTimes)
 }

 @Test
 fun testTimeoutSettings() {
  val config = ConfigBuilder()
   .setConnectTimeout(5)
   .setReadTimeout(10)
   .setWriteTimeout(15)
   .build()

  assertEquals(5, config.connectTimeout)
  assertEquals(10, config.readTimeout)
  assertEquals(15, config.writeTimeout)
 }

 @Test
 fun testAllTimeoutsSetTogether() {
  val config = ConfigBuilder().setAllTimeouts(20).build()
  assertEquals(20, config.connectTimeout)
  assertEquals(20, config.readTimeout)
  assertEquals(20, config.writeTimeout)
 }

 @Test
 fun testEnableFileLogger() {
  val file = File("my_logs")
  val config = ConfigBuilder().enableFileLogger(file).build()
  assertEquals(file, config.logDirectory)
 }

 @Test
 fun testEnableTempCookieJar() {
  val config = ConfigBuilder().enableTempCookieJar().build()
  assertTrue(config.enableTempCookieJar)
 }

 @Test
 fun testEnableResponseCachingWithTimeout() {
  val config = ConfigBuilder().enableResponseCaching(9999).build()
  assertTrue(config.enableResponseCaching)
  assertEquals(9999, config.responseCacheTimeout)
 }

 @Test
 fun testTrustAllCertsEnabled() {
  val config = ConfigBuilder().trustAllCerts().build()
  assertTrue(config.trustAllCerts)
 }
}
