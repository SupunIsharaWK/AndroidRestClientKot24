package com.supunishara.restclientkot24

/**
 * Standardized result codes for HTTP client behavior.
 * Used for mapping known client-side and network errors.
 *
 * These constants are returned by RestClientException or used
 * to identify common error cases during request execution.
 */
object Result {

    /** No result or unknown condition. */
    const val UNKNOWN = 0

    /** A generic exception occurred (not mapped to a specific type). */
    const val GENERIC_EXCEPTION = -1

    /** OkHttp library is missing or improperly linked. */
    const val OKHTTP_LIB_MISSING = -2

    /** Required Android permission is missing (e.g., INTERNET). */
    const val REQUIRED_PERMISSION_MISSING = -3

    /** No network connectivity available. */
    const val NOT_CONNECTED_TO_INTERNET = -4

    /** Invalid input or missing parameters in the request. */
    const val INVALID_PARAMETERS = -5

    /** The request timed out (socket timeout). */
    const val SOCKET_TIMEOUT = -6

    /** DNS resolution failed (e.g., no internet or bad hostname). */
    const val UNKNOWN_HOST = -7

    /** The URL was malformed or invalid. */
    const val MALFORMED_URL = -8

    /** Failed to establish a network connection (e.g., server down). */
    const val CONNECTION_EXCEPTION = -9

    /** Low-level socket error (e.g., connection reset, refused). */
    const val SOCKET_EXCEPTION = -10
}
