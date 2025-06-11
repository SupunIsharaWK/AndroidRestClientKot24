package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.Header
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class RestClientConfigTest {

 @Test
 fun testDefaultConfigBuilderValues() {
  val builder = ConfigBuilder()
  val config = builder.build()

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
 fun testCustomHeaderAdded() {
  val header = Header("Accept", "application/json")
  val config = ConfigBuilder().addHeader(header).build()
  assertTrue(config.headers.contains(header))
 }

 @Test
 fun testDefaultJsonHeaderApplied() {
  val config = ConfigBuilder().setDefaultJSONConfig().build()
  assertTrue(config.headers.any { it.name.equals("Accept", true) && it.value == "application/json" })
 }

 @Test
 fun testDebugFlagsEnabled() {
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
 fun testTimeoutsCanBeSet() {
  val config = ConfigBuilder()
   .setConnectTimeout(5)
   .setReadTimeout(7)
   .setWriteTimeout(9)
   .build()

  assertEquals(5, config.connectTimeout)
  assertEquals(7, config.readTimeout)
  assertEquals(9, config.writeTimeout)
 }

 @Test
 fun testAllTimeoutsSetTogether() {
  val config = ConfigBuilder().setAllTimeouts(15).build()
  assertEquals(15, config.connectTimeout)
  assertEquals(15, config.readTimeout)
  assertEquals(15, config.writeTimeout)
 }

 @Test
 fun testEnableFileLogger() {
  val file = File("dummy.log")
  val config = ConfigBuilder().enableFileLogger(file).build()
  assertEquals(file, config.logDirectory)
 }

 @Test
 fun testTrustAllCertsFlag() {
  val config = ConfigBuilder().trustAllCerts().build()
  assertTrue(config.trustAllCerts)
 }

 @Test
 fun testEnableTempCookieJar() {
  val config = ConfigBuilder().enableTempCookieJar().build()
  assertTrue(config.enableTempCookieJar)
 }

 @Test
 fun testEnableResponseCachingTimeout() {
  val config = ConfigBuilder().enableResponseCaching(1200).build()
  assertTrue(config.enableResponseCaching)
  assertEquals(1200, config.responseCacheTimeout)
 }
}
