package com.supunishara.restclientkot24.di

import com.supunishara.restclientkot24.network.RetrofitConfig
import dagger.BindsOptionalOf
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface OptionalRetrofitConfigModule {

    @BindsOptionalOf
    fun optionalRetrofitConfig(): RetrofitConfig
}
