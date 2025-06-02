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
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Header) return false
        return name.equals(other.name, true) && value == other.value
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

}
