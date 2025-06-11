package com.supunishara.restclientkot24.security

import okhttp3.CertificatePinner
import javax.inject.Inject

/**
 * Provides a CertificatePinner using the injected pin config.
 */
class CertificatePinnerProvider @Inject constructor(
    private val config: CertificatePinConfig
) {
    fun provide(): CertificatePinner {
        val builder = CertificatePinner.Builder()
        config.pins.forEach { (domain, pins) ->
            pins.forEach { pin ->
                builder.add(domain, pin)
            }
        }
        return builder.build()
    }
}