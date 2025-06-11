package com.supunishara.restclientkot24.security

/**
 * Configuration holder for domains and their SHA-256 pins.
 */
data class CertificatePinConfig(
    val pins: Map<String, List<String>>
)
