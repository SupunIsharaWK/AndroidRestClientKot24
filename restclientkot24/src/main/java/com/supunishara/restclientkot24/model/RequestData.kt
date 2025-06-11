package com.supunishara.restclientkot24.model

/**
 * A generic wrapper that encapsulates the details of an HTTP request.
 *
 * @param T The type of the request body (e.g., LoginRequest, FormData, etc.)
 * @param body Optional request body object
 * @param headers Optional map of HTTP headers
 * @param queryParams Optional map of query parameters
 */
data class RequestData<T>(
    var body: T? = null,
    var headers: Map<String, String>? = null,
    var queryParams: Map<String, String>? = null
) {
    override fun toString(): String {
        return "RequestData(body=$body, headers=$headers, queryParams=$queryParams)"
    }
}