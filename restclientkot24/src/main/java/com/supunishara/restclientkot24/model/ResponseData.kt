package com.supunishara.restclientkot24.model

/**
 * A generic wrapper for structured API responses.
 * Useful for APIs that return a standard response format like:
 * {
 *   "data": {...},
 *   "status": "success",
 *   "message": "Operation completed"
 * }
 *
 * @param T The type of the actual payload (e.g., User, Product)
 */
data class ResponseData<T>(
    var data: T? = null,
    var status: String? = null,
    var message: String? = null
) {
    override fun toString(): String {
        return "ResponseData(data=$data, status=$status, message=$message)"
    }
}
