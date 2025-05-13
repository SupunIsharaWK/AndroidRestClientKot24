package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.data_classes.Header
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MultipartBody
import org.json.JSONArray
import org.json.JSONObject
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLSocketFactory

class Request {
    private var httpUrl: HttpUrl? = null
    private var method: Method? = null
    private var headers: MutableList<Header>? = null
    private var rawBody: String? = null
    private var formBody: FormBody? = null
    private var multipartBody: MultipartBody? = null
    private var bodyType: BodyType? = null
    private var connectTimeout = 0
    private var readTimeout = 0
    private var writeTimeout = 0
    private var hostnameVerifier: HostnameVerifier? = null
    private var sslSocketFactory: SSLSocketFactory? = null
    private var trustAllCerts = false
    private var responseCachingStatus = 0
    private var responseCacheTimeout = 0
    private var cacheCallbackEnabled = false
    private var debugPrintInfo = 0
    private var debugPrintHeaders = 0
    private var debugPrintTimes = 0
    private var defaultHeaders = 0
    private var forceGzipDecode = false

    constructor(
        httpUrl: HttpUrl,
        method: Method,
        headers: MutableList<Header>,
        rawBody: String,
        formBody: FormBody,
        multipartBody: MultipartBody,
        bodyType: BodyType,
        connectTimeout: Int,
        readTimeout: Int,
        writeTimeout: Int,
        hostnameVerifier: HostnameVerifier,
        sslSocketFactory: SSLSocketFactory,
        trustAllCerts: Boolean,
        responseCachingStatus: Int,
        responseCacheTimeout: Int,
        debugPrintInfo: Int,
        debugPrintHeaders: Int,
        debugPrintTimes: Int,
        defaultHeaders: Int,
        forceGzipDecode: Boolean,
    ) {
        this.httpUrl = httpUrl
        this.method = method
        this.headers = headers
        this.rawBody = rawBody
        this.formBody = formBody
        this.multipartBody = multipartBody
        this.bodyType = bodyType
        this.connectTimeout = connectTimeout
        this.readTimeout = readTimeout
        this.writeTimeout = writeTimeout
        this.hostnameVerifier = hostnameVerifier
        this.sslSocketFactory = sslSocketFactory
        this.trustAllCerts = trustAllCerts
        this.responseCachingStatus = responseCachingStatus
        this.responseCacheTimeout = responseCacheTimeout
        this.debugPrintInfo = debugPrintInfo
        this.debugPrintHeaders = debugPrintHeaders
        this.debugPrintTimes = debugPrintTimes
        this.defaultHeaders = defaultHeaders
        this.forceGzipDecode = forceGzipDecode
    }

    constructor(url: String?, method: Method?) {
        this.bodyType = BodyType.EMPTY
        this.connectTimeout = 0
        this.readTimeout = 0
        this.writeTimeout = 0
        this.responseCachingStatus = 0
        this.responseCacheTimeout = -1
        this.cacheCallbackEnabled = true
        this.debugPrintInfo = 0
        this.debugPrintHeaders = 0
        this.debugPrintTimes = 0
        this.defaultHeaders = 0
        this.forceGzipDecode = false
        this.httpUrl = url!!.toHttpUrlOrNull()!!
        this.method = method
    }

    constructor(httpUrl: HttpUrl?, method: Method?) {
        this.bodyType = BodyType.EMPTY
        this.connectTimeout = 0
        this.readTimeout = 0
        this.writeTimeout = 0
        this.responseCachingStatus = 0
        this.responseCacheTimeout = -1
        this.cacheCallbackEnabled = true
        this.debugPrintInfo = 0
        this.debugPrintHeaders = 0
        this.debugPrintTimes = 0
        this.defaultHeaders = 0
        this.forceGzipDecode = false
        this.httpUrl = httpUrl!!
        this.method = method
    }

    enum class Method {
        GET,
        POST,
        PUT,
        PATCH,
        DELETE
    }

    enum class BodyType {
        EMPTY,
        X_FORM_URL_ENCODED,
        TEXT,
        JSON,
        XML,
        MULTIPART
    }

    companion object {
        fun getDefaultHeaders(request: Request): Int {
            return request.defaultHeaders
        }

        fun getDebugPrintTimes(request: Request): Int {
            return request.debugPrintTimes
        }

        fun getDebugPrintHeaders(request: Request): Int {
            return request.debugPrintHeaders
        }

        fun getDebugPrintInfo(request: Request): Int {
            return request.debugPrintInfo
        }

        fun isCacheCallbackEnabled(request: Request?): Boolean? {
            return request?.cacheCallbackEnabled
        }

        fun setResponseCacheTimeout(request: Request?, responseCacheTimeout: Int) {
            request?.responseCacheTimeout = responseCacheTimeout
        }

        fun getResponseCacheTimeout(request: Request): Int {
            return request.responseCacheTimeout
        }

        fun getResponseCachingStatus(request: Request): Int {
            return request.responseCachingStatus
        }

        fun setTrustAllCerts(request: Request, trustAllCerts: Boolean) {
            request.trustAllCerts = trustAllCerts
        }

        fun isTrustAllCerts(request: Request): Boolean {
            return request.trustAllCerts
        }

        fun setSslSocketFactory(request: Request, sslSocketFactory: SSLSocketFactory?) {
            request.sslSocketFactory = sslSocketFactory
        }

        fun getSslSocketFactory(request: Request): SSLSocketFactory? {
            return request.sslSocketFactory
        }

        fun setHostnameVerifier(request: Request, hostnameVerifier: HostnameVerifier?) {
            request.hostnameVerifier = hostnameVerifier
        }

        fun getHostnameVerifier(request: Request): HostnameVerifier? {
            return request.hostnameVerifier
        }

        fun setAllTimeouts(request: Request, seconds: Int) {
            request.connectTimeout = seconds
            request.readTimeout = seconds
            request.writeTimeout = seconds
        }

        fun getWriteTimeout(request: Request): Int {
            return request.writeTimeout
        }

        fun getHttpUrl(request: Request): HttpUrl? {
            return request.httpUrl
        }

        fun enableResponseCaching(request: Request) {
            request.responseCacheTimeout = 0
            request.responseCachingStatus = 1
        }

        fun setConnectTimeout(request: Request, connectTimeout: Int) {
            request.connectTimeout = connectTimeout
        }

        fun getConnectTimeout(request: Request): Int {
            return request.connectTimeout
        }

        fun enableResponseCaching(request: Request, timeout: Int) {
            request.responseCacheTimeout = timeout
            request.responseCachingStatus = 1
        }

        fun setReadTimeout(request: Request, readTimeout: Int) {
            request.readTimeout = readTimeout
        }

        fun getReadTimeout(request: Request): Int {
            return request.readTimeout
        }

        fun setWriteTimeout(request: Request, writeTimeout: Int) {
            request.writeTimeout = writeTimeout
        }

        fun getFormBody(request: Request): FormBody? {
            return request.formBody
        }

        fun getMethod(request: Request): Method? {
            return request.method
        }

        fun enableDebugPrintInfo(request: Request) {
            request.debugPrintInfo = 1
        }

        fun forceGzipDecode(request: Request) {
            request.forceGzipDecode = true
        }

        fun isForceGzipDecode(request: Request): Boolean {
            return request.forceGzipDecode
        }

        fun getBodyType(request: Request): BodyType? {
            return request.bodyType
        }

        fun getRawBody(request: Request): String? {
            return request.rawBody
        }

        fun enableDebugPrintHeaders(request: Request) {
            request.debugPrintHeaders = 1
        }

        fun enableDebugPrintTimes(request: Request) {
            request.debugPrintTimes = 1
        }

        fun disableResponseCaching(request: Request) {
            request.responseCachingStatus = 2
        }

        fun disableCacheCallback(request: Request) {
            request.cacheCallbackEnabled = false
        }

        fun disableDebugPrintInfo(request: Request) {
            request.debugPrintInfo = 2
        }

        fun disableDebugPrintHeaders(request: Request) {
            request.debugPrintHeaders = 2
        }

        fun disableDebugPrintTimes(request: Request) {
            request.debugPrintTimes = 2
        }

        fun disableDefaultHeaders(request: Request) {
            request.defaultHeaders = 2
        }

        fun setRawBody(request: Request, data: String?, bodyType: BodyType) {
            request.rawBody = data
            request.bodyType = bodyType
            if (bodyType == BodyType.JSON) {
                Companion.addHeader(
                    request,
                    Header("Content-Type", "application/json; charset=utf-8")
                )
            } else if (bodyType == BodyType.XML) {
                Companion.addHeader(
                    request,
                    Header("Content-Type", "application/xml; charset=utf-8")
                )
            }
        }

        fun setJSONString(request: Request, data: String?) {
            if (data != null && data.length > 0) {
                request.rawBody = data
                request.bodyType = BodyType.JSON
                Companion.addHeader(
                    request,
                    Header("Content-Type", "application/json; charset=utf-8")
                )
            } else {
                request.bodyType = BodyType.EMPTY
            }
        }

        fun setJSONObject(request: Request, data: JSONObject?) {
            if (data != null) {
                request.rawBody = data.toString()
                request.bodyType = BodyType.JSON
                Companion.addHeader(
                    request,
                    Header("Content-Type", "application/json; charset=utf-8")
                )
            } else {
                request.bodyType = BodyType.EMPTY
            }
        }

        fun setJSONArray(request: Request, data: JSONArray?) {
            if (data != null) {
                request.rawBody = data.toString()
                request.bodyType = BodyType.JSON
                Companion.addHeader(
                    request,
                    Header("Content-Type", "application/json; charset=utf-8")
                )
            } else {
                request.bodyType = BodyType.EMPTY
            }
        }

        fun setFormBody(request: Request, data: FormBody?) {
            request.formBody = data
            request.bodyType = BodyType.X_FORM_URL_ENCODED
            Companion.addHeader(
                request,
                Header("Content-Type", "application/x-www-form-urlencoded")
            )
        }

        fun getMultipartBody(request: Request): MultipartBody? {
            return request.multipartBody
        }

        fun addHeader(request: Request, header: Header?, replace: Boolean) {
            if (header == null) return  // early return if header is null

            val foundIndex = Companion.getHeaders(request)?.indexOf(header) ?: -1
            if (foundIndex > -1) {
                if (replace) {
                    Companion.getHeaders(request)?.set(foundIndex, header)
                }
            } else {
                Companion.getHeaders(request)?.add(header)
            }
        }

        fun setMultipartBody(request: Request, body: MultipartBody?) {
            request.bodyType = BodyType.MULTIPART
            request.multipartBody = body
        }

        fun setPlainText(request: Request, data: String?) {
            if (data != null && data.length > 0) {
                request.rawBody = data
                request.bodyType = BodyType.TEXT
                Companion.addHeader(request, Header("Content-Type", "text/plain; charset=utf-8"))
            } else {
                request.bodyType = BodyType.EMPTY
            }
        }

        fun addHeader(request: Request, header: Header?) {
            addHeader(request, header, true)
        }

        fun getHeaders(request: Request): MutableList<Header>? {
            if (request.headers == null) {
                request.headers = mutableListOf()
            }
            return request.headers
        }
    }
}


