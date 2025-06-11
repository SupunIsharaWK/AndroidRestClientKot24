package com.supunishara.restclientkot24.model

/**
 * Represents a single HTTP header key-value pair.
 * Optional abstraction over Map<String, String> if header logic becomes complex.
 */
data class Header(
    val key: String,
    val value: String
)