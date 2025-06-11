package com.supunishara.restclientkot24.model

/**
 * Enum representing common categories of network errors.
 * Helps classify failures for better client-side error handling.
 */
enum class NetworkErrorType {
    TIMEOUT,
    NO_CONNECTION,
    UNAUTHORIZED,
    NOT_FOUND,
    SERVER_ERROR,
    UNKNOWN
}