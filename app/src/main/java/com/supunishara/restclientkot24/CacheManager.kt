package com.supunishara.restclientkot24

import android.content.Context
import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.helpers.DatabaseHelper

/**
 * Persistent cache manager using [DatabaseHelper] for SQLite-based storage.
 * Supports:
 * - Add/update cache entries
 * - Retrieve entries (with expiration check)
 * - Remove specific entries
 * - Extendable for clearing all data
 */
object CacheManager {

    /**
     * Adds or updates a cache entry in the database.
     * Returns 0 if inserted, 1 if updated, or -1 if error.
     */
    fun addCacheEntry(context: Context, entry: CacheData): Int {
        return DatabaseHelper.getInstance(context).addCacheEntry(entry)
    }

    /**
     * Retrieves a valid, non-expired cache entry from the database.
     * @return [CacheData] or null if not found or expired
     */
    fun getCacheEntry(context: Context, method: String, url: String): CacheData? {
        val entry = DatabaseHelper.getInstance(context).getCacheData(url, method)
        return if (entry != null && !entry.isExpired()) entry else null
    }

    /**
     * Removes a specific cache entry from the database.
     */
    fun removeEntry(context: Context, method: String, url: String): Int {
        return DatabaseHelper.getInstance(context).deleteCacheData(url, method)
    }

    /**
     * Clears all cache data.
     * NOTE: Implement this if needed for full cache invalidation.
     */
    fun clearAll(context: Context) {
        // Optional: Not implemented in DB yet. You could drop or truncate the "cache" table.
    }
}
