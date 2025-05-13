package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

class RestClientConfig(configBuilder: ConfigBuilder) {
    var logDirectory: File? = null
    var debugPrintInfo: Boolean = false
    var debugPrintHeaders: Boolean = false
    var debugPrintTimes: Boolean = false
    var enableCache: Boolean = false
    var enableResponseCaching: Boolean = false
    var responseCacheTimeout: Int = 0
    var trustAllCerts: Boolean = false
    var connectTimeout: Int = 0
    var readTimeout: Int = 0
    var writeTimeout: Int = 0
    var enableTempCookieJar: Boolean = false
    var headers: List<Header>? = null
    var cookieJar: CookieJar? = null
    var hostnameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null

    init {
        this.logDirectory = configBuilder.logDirectory
        this.debugPrintInfo = configBuilder.debugPrintInfo
        this.debugPrintHeaders = configBuilder.debugPrintHeaders
        this.debugPrintTimes = configBuilder.debugPrintTimes
        this.enableCache = configBuilder.enableCache
        this.enableResponseCaching = configBuilder.enableResponseCaching
        this.responseCacheTimeout = configBuilder.responseCacheTimeout
        this.connectTimeout = configBuilder.connectTimeout
        this.readTimeout = configBuilder.readTimeout
        this.writeTimeout = configBuilder.writeTimeout
        this.headers = configBuilder.headers
        this.cookieJar = configBuilder.cookieJar
        this.hostnameVerifier = configBuilder.hostnameVerifier
        this.sslSocketFactory = configBuilder.sslSocketFactory
        this.trustAllCerts = configBuilder.trustAllCerts
        this.enableTempCookieJar = configBuilder.enableTempCookieJar
    }
}