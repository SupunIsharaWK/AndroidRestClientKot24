package com.supunishara.restclientkot24.data_classes

import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [28],
    manifest = Config.NONE
)  //@Config(sdk = [33]) // or any API level you want to simulate
class CacheDataTest {

    @Test
    fun isExpiredReturnsFalseIfTimeoutZero() {
        val cache = CacheData(timeout = 0, createdAt = System.currentTimeMillis())
        assertFalse(cache.isExpired())
    }

    @Test
    fun isExpiredReturnsFalseIfWithinTimeout() {
        val now = System.currentTimeMillis()
        val cache = CacheData(createdAt = now - 1000, timeout = 5000)
        assertFalse(cache.isExpired())
    }

    @Test
    fun isExpiredReturnsTrueIfTimeoutExceeded() {
        val now = System.currentTimeMillis()
        val cache = CacheData(createdAt = now - 6000, timeout = 5000)
        assertTrue(cache.isExpired())
    }

    @Test
    fun defaultValuesAreCorrectlyInitialized() {
        val default = CacheData()
        assertEquals("", default.url)
        assertEquals("", default.method)
        assertEquals("", default.data)
        assertEquals(0L, default.createdAt)
        assertEquals(0, default.timeout)
        assertFalse(default.status)
    }
}
