package com.supunishara.restclientkot24.model

/**
 * Configuration for a specific API call.
 * Allows overriding default behaviors like timeout or retry policy.
 *
 * @param timeoutInSeconds Optional override for request timeout.
 * @param shouldRetry Whether to retry the request if it fails.
 * @param forceCache Use cache even if network is available.
 */
data class ApiCallConfig(
    val timeoutInSeconds: Long? = null,
    val shouldRetry: Boolean = false,
    val forceCache: Boolean = false
)