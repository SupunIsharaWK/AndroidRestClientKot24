package com.supunishara.restclientkot24.exceptions

/**
 * Base exception used for all client-related errors in RestClient.
 *
 * Includes an errorCode to describe the specific cause
 * (e.g., timeout, network failure, permission issue).
 *
 * See [com.supunishara.restclientkot24.Result] for predefined error codes.
 */
open class RestClientException(
    val errorCode: Int,                  // Specific result code
    message: String,                     // User-friendly error message
    cause: Throwable? = null             // Optional root cause exception
) : Throwable(message, cause)
