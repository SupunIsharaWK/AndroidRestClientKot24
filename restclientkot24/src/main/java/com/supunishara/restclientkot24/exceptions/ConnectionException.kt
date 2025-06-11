package com.supunishara.restclientkot24.exceptions

/**
 * Represents network-related exceptions such as:
 * - Timeout
 * - DNS failure
 * - Connection refused
 * - Malformed URL
 *
 * Subclass of [RestClientException] with specific use for connectivity issues.
 */
class ConnectionException(
    code: Int,
    detailMessage: String,
    throwable: Throwable? = null
) : RestClientException(code, detailMessage, throwable)
