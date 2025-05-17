package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.RestClientException
import okhttp3.Call
import okhttp3.Response as OkHttpResponse

/**
 * Represents the result of an HTTP request executed by [RestClient].
 * Holds raw response data, metadata, and error/caching information.
 */
class Response(
    var request: Request? = null,                   // The original request
    var responseBody: String = "",                  // The raw string body returned from the server
    var httpResponse: OkHttpResponse? = null,       // OkHttp response object (nullable)
    var call: Call? = null,                         // OkHttp Call object
    var exception: RestClientException? = null,     // Any exception encountered during execution
    var cacheData: CacheData? = null                // Cached response (if applicable)
) {
    // Not used in refactored version but retained for compatibility
    private var responseCode = 0

    /**
     * The HTTP status code from the response.
     * Returns the exception code if a client-side error occurred.
     */
    val statusCode: Int
        get() = exception?.errorCode ?: httpResponse?.code ?: 0

    /**
     * The status message from the server or exception message if failed.
     */
    val statusMessage: String
        get() = exception?.toString() ?: httpResponse?.message ?: "Unknown response"

    /**
     * A map of all headers received in the response.
     */
    val headers: Map<String, List<String>>?
        get() = runCatching { httpResponse?.headers?.toMultimap() }.getOrNull()

    /**
     * Returns the list of header values for the given header [name].
     */
    fun headers(name: String): List<String>? =
        runCatching { httpResponse?.headers?.values(name) }.getOrNull()

    /**
     * Returns true if the response is successful (HTTP 2xx) or comes from cache.
     */
    val isSuccessful: Boolean
        get() = (httpResponse?.isSuccessful ?: false) || cacheData != null

    /**
     * True if the network call has already been executed.
     */
    val isExecuted: Boolean
        get() = call?.isExecuted() ?: false

    /**
     * True if an exception occurred during the request.
     */
    val isException: Boolean
        get() = exception != null

    /**
     * True if the data was retrieved from a local cache.
     */
    val isCache: Boolean
        get() = cacheData != null

    /**
     * Returns the body content as string.
     */
    override fun toString(): String = responseBody
}
