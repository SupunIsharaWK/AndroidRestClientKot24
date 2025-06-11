package com.supunishara.restclientkot24.helpers

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.supunishara.restclientkot24.data_classes.CacheData
import android.database.sqlite.SQLiteOpenHelper as SQLiteOpenHelper1

class DatabaseHelper : SQLiteOpenHelper1 {
    private val DATABASENAME: String = "restclient_lib.cache.db"
    private val DATABASEVERSION: Int = 1
    private var instance: DatabaseHelper? = null

    internal constructor(context: Context) : super(context, "restclient_lib.cache.db", null, 1)

    override fun onCreate(database: SQLiteDatabase?) {
        val sql =
            "CREATE TABLE IF NOT EXISTS cache (_id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT, method TEXT, data TEXT, created_at INTEGER, updated_at INTEGER, timeout INTEGER)"
        database?.execSQL(sql)
    }

    override fun onUpgrade(database: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        this.dropAllTables(database)
        this.onCreate(database)
    }

    fun dropAllTables(database: SQLiteDatabase?) {
        val sql = "DROP TABLE IF EXISTS cache"
        database?.execSQL(sql)
    }

    fun addCacheData(cacheData: CacheData) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("url", cacheData.url)
        contentValues.put("method", cacheData.method)
        contentValues.put("data", cacheData.data)
        contentValues.put("created_at", System.currentTimeMillis())
        contentValues.put("updated_at", System.currentTimeMillis())
        contentValues.put("timeout", cacheData.timeout)
        database.insert("cache", null, contentValues)
    }

    fun updateCacheData(cacheData: CacheData) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put("data", cacheData.data)
        contentValues.put("updated_at", System.currentTimeMillis())
        contentValues.put("timeout", cacheData.timeout)
        val params = (("url = '" + cacheData.url) + "' AND method = '" + cacheData.method + "'")
        database.update("cache", contentValues, params, null)
    }

    @SuppressLint("Range")
    fun getCacheData(url: String, method: String): CacheData? {
        val database = this.readableDatabase
        val sql = "select * from cache where url = $url AND method = $method"
        val cursor = database.rawQuery(sql, null)
        val cacheData: CacheData?
        if (cursor.moveToNext()) {
            cacheData = CacheData(
                url = cursor.getString(cursor.getColumnIndex("url")),
                method = cursor.getString(cursor.getColumnIndex("method")),
                createdAt = cursor.getLong(cursor.getColumnIndex("created_at")),
                updatedAt = cursor.getLong(cursor.getColumnIndex("updated_at")),
                timeout = cursor.getInt(cursor.getColumnIndex("timeout")),
                data = cursor.getString(cursor.getColumnIndex("data")),
                status = true
            )
        } else {
            cacheData = null
        }
        cursor.close()
        return cacheData
    }

    fun deleteCacheData(url: String, method: String): Int {
        val database = this.writableDatabase
        val params = "url = $url AND method = $method"
        return database.delete("cache", params, null)
    }

    fun addCacheEntry(cacheData: CacheData): Int {
        var result: Int
        try {
            val existing = getCacheData(cacheData.url, cacheData.method)
            if (existing != null) {
                updateCacheData(cacheData)
                result = 1
            } else {
                addCacheData(cacheData)
                result = 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            result = -1
        }
        return result
    }

    companion object {
        fun getInstance(context: Context?): DatabaseHelper {
            val databaseHelper = DatabaseHelper(context!!)
            if (databaseHelper.instance == null) {
                databaseHelper.instance = DatabaseHelper(context)
            }
            return databaseHelper.instance as DatabaseHelper
        }
    }

}