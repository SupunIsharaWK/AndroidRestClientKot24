package com.supunishara.restclientkot24.configs

import com.supunishara.restclientkot24.RestClientConfig
import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 * A builder class for constructing [RestClientConfig] with a fluent DSL.
 *
 * Usage example:
 * ```
 * val config = ConfigBuilder()
 *     .setConnectTimeout(30)
 *     .addHeader(Header("Authorization", "Bearer token"))
 *     .enableResponseCaching(60_000)
 *     .build()
 * ```
 */
class ConfigBuilder {

    var logDirectory: File? = null

    var debugPrintInfo: Boolean = false
    var debugPrintHeaders: Boolean = false
    var debugPrintTimes: Boolean = false

    var enableCache: Boolean = false
    var enableResponseCaching: Boolean = false
    var responseCacheTimeout: Int = 86_400_000  // 24 hours by default

    var trustAllCerts: Boolean = false

    var connectTimeout: Int = 10  // seconds
    var readTimeout: Int = 10     // seconds
    var writeTimeout: Int = 10    // seconds

    var enableTempCookieJar: Boolean = false

    var headers: MutableList<Header>? = null
    var cookieJar: CookieJar? = null
    var hostnameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null

    /**
     * Sets default Accept header for JSON APIs.
     */
    fun setDefaultJSONConfig(): ConfigBuilder {
        if (headers == null) headers = mutableListOf()
        headers?.add(Header("Accept", "application/json"))
        return this
    }

    /**
     * Adds a custom header to the config.
     */
    fun addHeader(header: Header?): ConfigBuilder {
        if (headers == null) headers = mutableListOf()
        header?.let { headers?.add(it) }
        return this
    }

    /** Enables debug logging for request/response info. */
    fun debugPrintInfo(): ConfigBuilder {
        this.debugPrintInfo = true
        return this
    }

    /** Enables debug logging for headers. */
    fun debugPrintHeaders(): ConfigBuilder {
        this.debugPrintHeaders = true
        return this
    }

    /** Enables debug logging for timing metrics. */
    fun debugPrintTimes(): ConfigBuilder {
        this.debugPrintTimes = true
        return this
    }

    /**
     * Enables file-based logging to a given [logDirectory].
     */
    fun enableFileLogger(logDirectory: File?): ConfigBuilder {
        this.logDirectory = logDirectory
        return this
    }

    /**
     * Enables general-purpose response or event caching.
     */
    fun enableCache(): ConfigBuilder {
        this.enableCache = true
        return this
    }

    /**
     * Enables HTTP response caching and sets timeout in milliseconds.
     */
    fun enableResponseCaching(timeout: Int): ConfigBuilder {
        this.responseCacheTimeout = timeout
        this.enableResponseCaching = true
        return this
    }

    /**
     * Assigns a custom [CookieJar] for persistent session handling.
     */
    fun enableCookieJar(jar: CookieJar?): ConfigBuilder {
        this.cookieJar = jar
        return this
    }

    /**
     * Allows all SSL certificates (not recommended for production).
     */
    fun trustAllCerts(): ConfigBuilder {
        this.trustAllCerts = true
        return this
    }

    /**
     * Sets the connection timeout (in seconds).
     */
    fun setConnectTimeout(seconds: Int): ConfigBuilder {
        this.connectTimeout = seconds
        return this
    }

    /**
     * Sets the read timeout (in seconds).
     */
    fun setReadTimeout(seconds: Int): ConfigBuilder {
        this.readTimeout = seconds
        return this
    }

    /**
     * Sets the write timeout (in seconds).
     */
    fun setWriteTimeout(seconds: Int): ConfigBuilder {
        this.writeTimeout = seconds
        return this
    }

    /**
     * Sets connect, read, and write timeouts all to the same value (in seconds).
     */
    fun setAllTimeouts(seconds: Int): ConfigBuilder {
        this.connectTimeout = seconds
        this.readTimeout = seconds
        this.writeTimeout = seconds
        return this
    }

    /**
     * Assigns a custom [HostnameVerifier] for SSL checks.
     */
    fun setHostnameVerifier(verifier: HostnameVerifier?): ConfigBuilder {
        this.hostnameVerifier = verifier
        return this
    }

    /**
     * Assigns a custom [SSLSocketFactory] for secure communication.
     */
    fun setSSLSocketFactory(factory: SSLSocketFactory?): ConfigBuilder {
        this.sslSocketFactory = factory
        return this
    }

    /**
     * Enables an in-memory, non-persistent cookie jar.
     */
    fun enableTempCookieJar(): ConfigBuilder {
        this.enableTempCookieJar = true
        return this
    }

    /**
     * Builds a [RestClientConfig] using the current configuration.
     */
    fun build(): RestClientConfig {
        return RestClientConfig(this)
    }
}
