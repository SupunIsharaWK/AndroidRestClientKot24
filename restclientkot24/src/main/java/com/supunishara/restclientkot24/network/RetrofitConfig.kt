package com.supunishara.restclientkot24.network

/**
 * Holds all API base URLs needed by the library.
 * Should be provided by the host app.
 */
data class RetrofitConfig(
    val mainApiBaseUrl: String,
    val authApiBaseUrl: String
)
