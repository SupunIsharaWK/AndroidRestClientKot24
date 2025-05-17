package com.supunishara.restclientkot24

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.supunishara.restclientkot24.data_classes.CacheData
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class CacheManagerTest {

 private lateinit var context: Context
 private lateinit var cacheData: CacheData

 @Before
 fun setup() {
  context = ApplicationProvider.getApplicationContext()
  cacheData = CacheData(
   url = "https://example.com",
   method = "GET",
   data = "{\"msg\":\"cached response\"}",
   createdAt = System.currentTimeMillis(),
   updatedAt = System.currentTimeMillis(),
   timeout = 5000,
   status = true
  )
 }

 @After
 fun tearDown() {
  CacheManager.removeEntry(context, cacheData.method, cacheData.url)
 }

 @Test
 fun testAddAndRetrieveCacheEntry() {
  val added = CacheManager.addCacheEntry(context, cacheData)
  assertTrue(added >= 0)

  val retrieved = CacheManager.getCacheEntry(context, "GET", "https://example.com")
  assertNotNull(retrieved)
  assertEquals(cacheData.data, retrieved!!.data)
 }

 @Test
 fun testUpdateExistingCacheEntry() {
  CacheManager.addCacheEntry(context, cacheData)

  val updatedData = cacheData.copy(data = "{\"msg\":\"updated\"}", timeout = 10000)
  CacheManager.addCacheEntry(context, updatedData)

  val result = CacheManager.getCacheEntry(context, "GET", "https://example.com")
  assertNotNull(result)
  assertEquals("{\"msg\":\"updated\"}", result!!.data)
  assertEquals(10000, result.timeout)
 }

 @Test
 fun testGetCacheReturnsNullIfNotFound() {
  val result = CacheManager.getCacheEntry(context, "GET", "https://nonexistent.com")
  assertNull(result)
 }

 @Test
 fun testDeleteCacheEntry() {
  CacheManager.addCacheEntry(context, cacheData)
  val deletedCount = CacheManager.removeEntry(context, "GET", "https://example.com")
  assertTrue(deletedCount >= 0)

  val result = CacheManager.getCacheEntry(context, "GET", "https://example.com")
  assertNull(result)
 }

 @Test
 fun testIsExpiredReturnsCorrectValue() {
  val fresh = cacheData.copy(createdAt = System.currentTimeMillis(), timeout = 5000)
  val expired = cacheData.copy(createdAt = System.currentTimeMillis() - 6000, timeout = 5000)

  assertFalse(fresh.isExpired())
  assertTrue(expired.isExpired())
 }
}
