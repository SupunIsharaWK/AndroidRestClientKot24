package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

/**
 * Immutable configuration holder for [RestClient].
 * Use [ConfigBuilder] to construct an instance.
 *
 * This class encapsulates all client-level settings such as
 * timeouts, headers, caching, logging flags, and SSL settings.
 */
class RestClientConfig(configBuilder: ConfigBuilder) {

    /** Optional directory to store logs, if enabled. */
    val logDirectory: File? = configBuilder.logDirectory

    /** If true, request/response info will be printed to log. */
    val debugPrintInfo: Boolean = configBuilder.debugPrintInfo

    /** If true, request/response headers will be printed to log. */
    val debugPrintHeaders: Boolean = configBuilder.debugPrintHeaders

    /** If true, request/response timing will be printed to log. */
    val debugPrintTimes: Boolean = configBuilder.debugPrintTimes

    /** If true, enables local request caching (e.g., to database or memory). */
    val enableCache: Boolean = configBuilder.enableCache

    /** If true, successful HTTP responses are eligible for caching. */
    val enableResponseCaching: Boolean = configBuilder.enableResponseCaching

    /** Cache duration for HTTP responses in milliseconds. */
    val responseCacheTimeout: Int = configBuilder.responseCacheTimeout

    /** If true, all SSL certificates will be trusted (not recommended for production). */
    val trustAllCerts: Boolean = configBuilder.trustAllCerts

    /** Connection timeout in milliseconds. */
    val connectTimeout: Int = configBuilder.connectTimeout

    /** Read timeout in milliseconds. */
    val readTimeout: Int = configBuilder.readTimeout

    /** Write timeout in milliseconds. */
    val writeTimeout: Int = configBuilder.writeTimeout

    /** If true, uses a temporary in-memory cookie jar (non-persistent). */
    val enableTempCookieJar: Boolean = configBuilder.enableTempCookieJar

    /** Default headers to include in every request. */
    val headers: List<Header> = configBuilder.headers ?: emptyList()

    /** Custom [CookieJar] implementation for request state persistence. */
    val cookieJar: CookieJar? = configBuilder.cookieJar

    /** Optional hostname verifier for SSL. */
    val hostnameVerifier: HostnameVerifier? = configBuilder.hostnameVerifier

    /** Optional custom SSL socket factory for secure connections. */
    val sslSocketFactory: SSLSocketFactory? = configBuilder.sslSocketFactory
}
