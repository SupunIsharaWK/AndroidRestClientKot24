package com.supunishara.restclientkot24.data_classes

/**
 * Represents a single HTTP header as a name-value pair.
 *
 * This class uses value-based equality — two headers are equal if both
 * the [name] and [value] are the same (case-sensitive by default).
 *
 * Example:
 * ```
 * Header("Authorization", "Bearer token")
 * Header("Content-Type", "application/json")
 * ```
 */
data class Header(
    val name: String,
    val value: String
)
