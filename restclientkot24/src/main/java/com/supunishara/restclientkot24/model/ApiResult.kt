package com.supunishara.restclientkot24.model

/**
 * A sealed class representing the result of an API call.
 * It can be either a Success, an Error, or a Loading state.
 */
sealed class ApiResult<out T> {

    /**
     * Represents a successful API response containing data of type T.
     */
    data class Success<T>(
        val data: T
    ) : ApiResult<T>() {
        override fun toString(): String = "Success(data=$data)"
    }

    /**
     * Represents an error occurred during the API call.
     * @param code Optional HTTP status code
     * @param message Optional error message
     * @param throwable Optional throwable that caused the error
     */
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val throwable: Throwable? = null
    ) : ApiResult<Nothing>() {
        override fun toString(): String = "Error(code=$code, message=$message, throwable=${throwable?.message})"
    }

    /**
     * Represents a loading state when the API call is in progress.
     */
    object Loading : ApiResult<Nothing>() {
        override fun toString(): String = "Loading"
    }
}