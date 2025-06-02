package com.supunishara.restclientkot24

import android.content.Context
import com.supunishara.restclientkot24.CacheManager
import com.supunishara.restclientkot24.data_classes.CacheData
import org.junit.Before
import org.junit.Test


class CacheManagerTest {

 private lateinit var context: Context

 @Before
 fun setUp() {
  context = Mockito.mock(Context::class.java)
 }

 @Test
 fun testAddAndRetrieveCacheEntry() {
  val cacheData = CacheData(
   url = "https://example.com",
   method = "get",
   data = "test_response",
   timeout = 10000,
   createdAt = System.currentTimeMillis(),
   updatedAt = System.currentTimeMillis()
  )

  val result = CacheManager.addCacheEntry(context, cacheData)
  assertTrue(result == 0 || result == 1)

  val retrieved = CacheManager.getCacheEntry(context, "get", "https://example.com")
  assertNotNull(retrieved)
  assertEquals("test_response", retrieved?.data)
 }

 @Test
 fun testUpdateExistingCacheEntry() {
  val initial = CacheData(
   url = "https://example.com",
   method = "get",
   data = "old_data",
   timeout = 10000,
   createdAt = System.currentTimeMillis(),
   updatedAt = System.currentTimeMillis()
  )
  CacheManager.addCacheEntry(context, initial)

  val updated = initial.copy(data = "new_data", updatedAt = System.currentTimeMillis())
  CacheManager.addCacheEntry(context, updated)

  val retrieved = CacheManager.getCacheEntry(context, "get", "https://example.com")
  assertEquals("new_data", retrieved?.data)
 }

 @Test
 fun testDeleteCacheEntry() {
  val cacheData = CacheData(
   url = "https://to-delete.com",
   method = "get",
   data = "to_delete",
   timeout = 10000,
   createdAt = System.currentTimeMillis(),
   updatedAt = System.currentTimeMillis()
  )

  CacheManager.addCacheEntry(context, cacheData)
  val result = CacheManager.removeEntry(context, "get", "https://to-delete.com")
  assertTrue(result >= 0)
  val retrieved = CacheManager.getCacheEntry(context, "get", "https://to-delete.com")
  assertNull(retrieved)
 }

 @Test
 fun testGetCacheReturnsNullIfNotFound() {
  val retrieved = CacheManager.getCacheEntry(context, "get", "https://nonexistent.com")
  assertNull(retrieved)
 }

 @Test
 fun testIsExpiredReturnsCorrectValue() {
  val expired = CacheData(
   url = "https://expired.com",
   method = "get",
   data = "expired",
   timeout = 1,
   createdAt = System.currentTimeMillis() - 10_000,
   updatedAt = System.currentTimeMillis() - 10_000
  )
  assertTrue(expired.isExpired())

  val valid = expired.copy(timeout = 60_000, updatedAt = System.currentTimeMillis())
  assertFalse(valid.isExpired())
 }
}
