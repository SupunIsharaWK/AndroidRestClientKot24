package com.supunishara.restclientkot24.data_classes

/**
 * Represents a cached API response entry.
 *
 * This data class holds metadata and content for a previously
 * executed HTTP request, allowing it to be reused or served offline.
 */
data class CacheData(

    /** The full request URL (e.g., https://api.example.com/data) */
    var url: String = "",

    /** The HTTP method used (e.g., "get", "post") */
    var method: String = "",

    /** The raw response body data to be cached */
    var data: String = "",

    /** Timestamp when this cache entry was created (in millis) */
    var createdAt: Long = 0L,

    /** Timestamp when this cache entry was last updated (in millis) */
    var updatedAt: Long = 0L,

    /** Timeout duration in milliseconds before cache is considered stale */
    var timeout: Int = 0,

    /** Whether the cache is still considered valid */
    var status: Boolean = false
) {
    /**
     * Checks if the cache entry has expired based on [timeout] and [createdAt].
     * @return true if expired, false otherwise
     */
    fun isExpired(): Boolean {
        if (timeout <= 0) return false  // Treat 0 or less as "never expires"
        val age = System.currentTimeMillis() - createdAt
        return age > timeout
    }
}
