package com.supunishara.restclientkot24.model

/**
 * A model class to represent error responses returned from the server.
 * Useful when API returns a structured error body (non-2xx responses).
 *
 * Example JSON:
 * {
 *   "error": "invalid_token",
 *   "message": "The token is expired",
 *   "statusCode": 401
 * }
 */
data class ErrorResponse(
    var error: String? = null,
    var message: String? = null,
    var statusCode: Int = 0
) {
    override fun toString(): String {
        return "ErrorResponse(error=$error, message=$message, statusCode=$statusCode)"
    }
}
