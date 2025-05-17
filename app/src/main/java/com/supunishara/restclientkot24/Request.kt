package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 * Represents a configurable HTTP request to be sent via [RestClient].
 * Includes URL, method, headers, body data, timeouts, and debug options.
 */
class Request(
    var httpUrl: HttpUrl? = null,
    var method: Method = Method.GET
) {

    // Custom request headers
    val restAPIHeaders: MutableList<Header> = mutableListOf()

    // Raw body content (e.g., JSON or plain text)
    var rawBody: String? = null

    // Form-encoded body
    var formBody: FormBody? = null

    // Multipart body (file + fields)
    var multipartBody: MultipartBody? = null

    // What type of body is being sent
    var bodyType: BodyType = BodyType.EMPTY

    // Timeout settings in milliseconds
    var connectTimeout: Int = 0
    var readTimeout: Int = 0
    var writeTimeout: Int = 0

    // SSL options
    var hostnameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null
    var trustAllCerts: Boolean = false

    // Response caching settings
    var responseCachingStatus: Int = 0  // 0 = none, 1 = enabled, 2 = disabled
    var responseCacheTimeout: Int = -1  // in milliseconds
    var cacheCallbackEnabled: Boolean = true

    // Debug options
    var debugPrintInfo: Boolean = false
    var debugPrintHeaders: Boolean = false
    var debugPrintTimes: Boolean = false

    // Behavior flags
    var useDefaultHeaders: Boolean = true
    var forceGzipDecode: Boolean = false

    /**
     * Add a new header to this request.
     * If [replace] is true and the key already exists, it will be replaced.
     */
    fun addHeader(header: Header, replace: Boolean = true) {
        val index = restAPIHeaders.indexOfFirst { it.name.equals(header.name, ignoreCase = true) }
        if (index != -1 && replace) restAPIHeaders[index] = header
        else if (index == -1) restAPIHeaders.add(header)
    }

    /**
     * Returns a read-only copy of the headers list.
     */
    fun getHeaders(): List<Header> = restAPIHeaders.toList()

    /**
     * Remove a header by key (case-insensitive).
     */
    fun removeHeader(key: String) {
        restAPIHeaders.removeAll { it.name.equals(key, ignoreCase = true) }
    }

    /**
     * Remove all headers.
     */
    fun clearHeaders() = restAPIHeaders.clear()

    /**
     * Sets a [JSONObject] body as JSON.
     */
    fun setBody(json: JSONObject?) {
        if (json != null) {
            rawBody = json.toString()
            bodyType = BodyType.JSON
            addHeader(Header("Content-Type", "application/json; charset=utf-8"))
        }
    }

    /**
     * Sets a [JSONArray] body as JSON.
     */
    fun setBody(jsonArray: JSONArray?) {
        if (jsonArray != null) {
            rawBody = jsonArray.toString()
            bodyType = BodyType.JSON
            addHeader(Header("Content-Type", "application/json; charset=utf-8"))
        }
    }

    /**
     * Sets a raw text body (e.g., plain text, JSON, or XML).
     * Automatically applies the appropriate Content-Type.
     */
    fun setBody(text: String?, type: BodyType = BodyType.TEXT) {
        if (!text.isNullOrEmpty()) {
            rawBody = text
            bodyType = type
            when (type) {
                BodyType.TEXT -> addHeader(Header("Content-Type", "text/plain; charset=utf-8"))
                BodyType.JSON -> addHeader(
                    Header(
                        "Content-Type",
                        "application/json; charset=utf-8"
                    )
                )

                BodyType.XML -> addHeader(Header("Content-Type", "application/xml; charset=utf-8"))
                else -> Unit
            }
        } else {
            bodyType = BodyType.EMPTY
        }
    }

    /**
     * Sets the request's form body and body type.
     */
    fun setBody(form: FormBody?) {
        formBody = form
        bodyType = BodyType.X_FORM_URL_ENCODED
        addHeader(Header("Content-Type", "application/x-www-form-urlencoded"))
    }

    /**
     * Sets the multipart body and type.
     */
    fun setBody(multipart: MultipartBody?) {
        multipartBody = multipart
        bodyType = BodyType.MULTIPART
    }

    /**
     * Sets all timeout values to the same value in milliseconds.
     */
    fun setTimeouts(timeout: Int) {
        connectTimeout = timeout
        readTimeout = timeout
        writeTimeout = timeout
    }

    /**
     * Enables response caching for this request with optional timeout (ms).
     */
    fun enableResponseCaching(timeout: Int = 0) {
        responseCacheTimeout = timeout
        responseCachingStatus = 1
    }

    /**
     * Enables response caching for this request.
     */
    fun enableResponseCaching() {
        responseCachingStatus = 1
    }

    /**
     * Disables response caching for this request.
     */
    fun disableResponseCaching() {
        responseCachingStatus = 2
    }

    /**
     * Supported HTTP methods.
     */
    enum class Method {
        GET, POST, PUT, PATCH, DELETE
    }

    /**
     * Body types supported by this client.
     */
    enum class BodyType {
        EMPTY,
        X_FORM_URL_ENCODED,
        TEXT,
        JSON,
        XML,
        MULTIPART
    }

    companion object {
        /**
         * Constructs a [Request] from a string URL and method.
         * Will throw if the URL is invalid.
         */
        fun fromUrl(url: String?, method: Method = Method.GET): Request {
            val httpUrl = url?.toHttpUrlOrNull() ?: throw IllegalArgumentException("Invalid URL")
            return Request(httpUrl, method)
        }
    }
}
