package com.supunishara.restclientkot24.configs

import com.supunishara.restclientkot24.RestClientConfig
import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.CookieJar
import java.io.File
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

class ConfigBuilder {
    var logDirectory: File? = null
    var debugPrintInfo: Boolean = false
    var debugPrintHeaders: Boolean = false
    var debugPrintTimes: Boolean = false
    var enableCache: Boolean = false
    var enableResponseCaching: Boolean = false
    var responseCacheTimeout: Int = 86400000
    var trustAllCerts: Boolean = false
    var connectTimeout: Int = 10
    var readTimeout: Int = 10
    var writeTimeout: Int = 10
    var enableTempCookieJar: Boolean = false
    var headers: MutableList<Header>? = null
    var cookieJar: CookieJar? = null
    var hostnameVerifier: HostnameVerifier? = null
    var sslSocketFactory: SSLSocketFactory? = null

    fun setDefaultJSONConfig(): ConfigBuilder {
        if (this.headers == null) {
            this.headers = mutableListOf()
        }

        headers!!.add(Header("Accept", "application/json"))
        return this
    }

    fun addHeader(header: Header?): ConfigBuilder {
        if (this.headers == null) {
            this.headers = mutableListOf()
        }

        if (header != null) {
            headers!!.add(header)
        }
        return this
    }

    fun debugPrintInfo(): ConfigBuilder {
        this.debugPrintInfo = true
        return this
    }

    fun debugPrintHeaders(): ConfigBuilder {
        this.debugPrintHeaders = true
        return this
    }

    fun debugPrintTimes(): ConfigBuilder {
        this.debugPrintTimes = true
        return this
    }

    fun enableFileLogger(logDirectory: File?): ConfigBuilder {
        this.logDirectory = logDirectory
        return this
    }

    fun enableCache(): ConfigBuilder {
        this.enableCache = true
        return this
    }

    fun enableResponseCaching(timeout: Int): ConfigBuilder {
        this.responseCacheTimeout = timeout
        this.enableResponseCaching = true
        return this
    }

    fun enableCookieJar(jar: CookieJar?): ConfigBuilder {
        this.cookieJar = jar
        return this
    }

    fun trustAllCerts(): ConfigBuilder {
        this.trustAllCerts = true
        return this
    }

    fun setConnectTimeout(seconds: Int): ConfigBuilder {
        this.connectTimeout = seconds
        return this
    }

    fun setReadTimeout(seconds: Int): ConfigBuilder {
        this.readTimeout = seconds
        return this
    }

    fun setWriteTimeout(seconds: Int): ConfigBuilder {
        this.writeTimeout = seconds
        return this
    }

    fun setAllTimeouts(seconds: Int): ConfigBuilder {
        this.connectTimeout = seconds
        this.readTimeout = seconds
        this.writeTimeout = seconds
        return this
    }

    fun setHostnameVerifier(verifier: HostnameVerifier?): ConfigBuilder {
        this.hostnameVerifier = verifier
        return this
    }

    fun setSSLSocketFactory(factory: SSLSocketFactory?): ConfigBuilder {
        this.sslSocketFactory = factory
        return this
    }

    fun enableTempCookieJar(): ConfigBuilder {
        this.enableTempCookieJar = true
        return this
    }

    fun build(): RestClientConfig {
        return RestClientConfig(this)
    }
}