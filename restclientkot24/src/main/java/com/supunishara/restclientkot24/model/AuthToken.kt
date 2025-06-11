package com.supunishara.restclientkot24.model

/**
 * Represents an authentication token.
 * Useful for storing access/refresh tokens and expiry logic.
 */
data class AuthToken(
    val accessToken: String,
    val refreshToken: String? = null,
    val expiresAt: Long? = null
)