package com.supunishara.restclientkot24.di

import com.supunishara.restclientkot24.network.RetrofitConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    @Named("mainApi")
    fun provideMainRetrofit(
        okHttpClient: OkHttpClient,
        optionalConfig: dagger.Lazy<RetrofitConfig>
    ): Retrofit {
        val configMain = try {
            optionalConfig.get()
        } catch (e: Exception) {
            RetrofitConfig(
                mainApiBaseUrl = "http://localhost:8080/",
                authApiBaseUrl = "http://localhost:8081/"
            )
        }

        return Retrofit.Builder()
            .baseUrl(configMain.mainApiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("authApi")
    fun provideAuthRetrofit(
        okHttpClient: OkHttpClient,
        optionalConfig: dagger.Lazy<RetrofitConfig>
    ): Retrofit {
        val configAuth = try {
            optionalConfig.get()
        } catch (e: Exception) {
            RetrofitConfig(
                mainApiBaseUrl = "http://localhost:8080/",
                authApiBaseUrl = "http://localhost:8081/"
            )
        }

        return Retrofit.Builder()
            .baseUrl(configAuth.authApiBaseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
