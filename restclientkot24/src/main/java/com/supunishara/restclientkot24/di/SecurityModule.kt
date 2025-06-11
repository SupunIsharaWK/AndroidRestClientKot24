package com.supunishara.restclientkot24.di

import com.supunishara.restclientkot24.security.CertificatePinConfig
import com.supunishara.restclientkot24.security.CertificatePinnerProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SecurityModule {

    @Provides
    @Singleton
    fun provideCertificatePinConfig(): CertificatePinConfig {
        return CertificatePinConfig(
            pins = mapOf(
                "your.domain.com" to listOf("sha256/AAAAAAAA..."),
                "api.other.com" to listOf("sha256/BBBBBBBB...")
            )
        )
    }

    @Provides
    @Singleton
    fun provideCertificatePinnerProvider(
        config: CertificatePinConfig
    ): CertificatePinnerProvider {
        return CertificatePinnerProvider(config)
    }
}