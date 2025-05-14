package com.supunishara.restclientkot24

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.util.Log
import com.supunishara.restclientkot24.Request.Companion.addHeader
import com.supunishara.restclientkot24.Request.Companion.getBodyType
import com.supunishara.restclientkot24.Request.Companion.getConnectTimeout
import com.supunishara.restclientkot24.Request.Companion.getDebugPrintHeaders
import com.supunishara.restclientkot24.Request.Companion.getDebugPrintInfo
import com.supunishara.restclientkot24.Request.Companion.getDebugPrintTimes
import com.supunishara.restclientkot24.Request.Companion.getDefaultHeaders
import com.supunishara.restclientkot24.Request.Companion.getFormBody
import com.supunishara.restclientkot24.Request.Companion.getHeaders
import com.supunishara.restclientkot24.Request.Companion.getHostnameVerifier
import com.supunishara.restclientkot24.Request.Companion.getHttpUrl
import com.supunishara.restclientkot24.Request.Companion.getMethod
import com.supunishara.restclientkot24.Request.Companion.getMultipartBody
import com.supunishara.restclientkot24.Request.Companion.getRawBody
import com.supunishara.restclientkot24.Request.Companion.getReadTimeout
import com.supunishara.restclientkot24.Request.Companion.getResponseCacheTimeout
import com.supunishara.restclientkot24.Request.Companion.getResponseCachingStatus
import com.supunishara.restclientkot24.Request.Companion.getSslSocketFactory
import com.supunishara.restclientkot24.Request.Companion.getWriteTimeout
import com.supunishara.restclientkot24.Request.Companion.isCacheCallbackEnabled
import com.supunishara.restclientkot24.Request.Companion.isForceGzipDecode
import com.supunishara.restclientkot24.Request.Companion.isTrustAllCerts
import com.supunishara.restclientkot24.Request.Method.DELETE
import com.supunishara.restclientkot24.Request.Method.GET
import com.supunishara.restclientkot24.Request.Method.PATCH
import com.supunishara.restclientkot24.Request.Method.POST
import com.supunishara.restclientkot24.Request.Method.PUT
import com.supunishara.restclientkot24.callbacks.ResponseCallbacks
import com.supunishara.restclientkot24.configs.ConfigBuilder
import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.data_classes.Header
import com.supunishara.restclientkot24.exceptions.ConnectionException
import com.supunishara.restclientkot24.exceptions.RestClientException
import com.supunishara.restclientkot24.helpers.DatabaseHelper.Companion.getInstance
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.CookieJar
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStreamReader
import java.net.ConnectException
import java.net.MalformedURLException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.text.SimpleDateFormat
import java.util.Arrays
import java.util.Date
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class RestClient {
    var config: RestClientConfig? = null
    var client: OkHttpClient? = null
    var requestCount: Int = 0

    @SuppressLint("SimpleDateFormat")
    private val dateFormatFileName = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    private val dateFormatLogEntry = SimpleDateFormat("yyyy-MM-dd HH:mm:ss:ms")

    @SuppressLint("SimpleDateFormat")
    private var dateFormatRequestId = SimpleDateFormat("ddHHmmss")
    private var logDirectory: File? = null
    private var context: Context? = null
    private var request: Request? = null
    private var callback: ResponseCallbacks? = null
    private var call: Call? = null
    private var requestId = ""
    private var executionStart = 0L
    private var enableResponseCaching = false
    private var responseCacheTimeout = 0
    private var debugPrintInfo = false
    private var debugPrintHeaders = false
    private var debugPrintTimes = false
    private var forceGzipDecode = false
    private var trustManagerFactory: TrustManagerFactory? = null
    private var trustManagers: TrustManager? = null
    private var sslSocketFactory: SSLSocketFactory? = null

    constructor(context: Context, request: Request, callbacks: ResponseCallbacks) {
        try {
            try {
                Class.forName("okhttp3.OkHttpClient")
            } catch (e1: ClassNotFoundException) {
                throw RestClientException(-2, "OkHttp3 library is missing or unable to load.", e1)
            }
            if (context.checkCallingOrSelfPermission("android.permission.INTERNET") != PackageManager.PERMISSION_GRANTED) {
                throw RestClientException(-3, "Permission missing: android.permission.INTERNET")
            }

            if (context.checkCallingOrSelfPermission("android.permission.ACCESS_NETWORK_STATE") != PackageManager.PERMISSION_GRANTED) {
                throw RestClientException(
                    -3,
                    "Permission missing: android.permission.ACCESS_NETWORK_STATE"
                )
            }

            if (context == null) {
                throw RestClientException(-5, "Context is null")
            }

            if (request == null) {
                throw RestClientException(-5, "Request is null")
            }

            this.context = context
            this.request = request
            if (config == null) {
                config = ConfigBuilder().build()
            }
            if (config!!.logDirectory != null) {
                if (context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED) {
                    throw RestClientException(-3, "android.permission.WRITE_EXTERNAL_STORAGE")
                }
                this.logDirectory = config!!.logDirectory
            }

            var builder: OkHttpClient.Builder
            if (client == null) {
                builder = OkHttpClient.Builder()
                if (config!!.enableCache) {
                    val cacheSize = 10485760
                    val cacheDirectory = context.getDir("rest_client_cache", 0)
                    val cache = Cache(cacheDirectory, cacheSize.toLong())
                    builder.cache(cache)
                }

                if (config!!.cookieJar != null) {
                    builder.cookieJar(config!!.cookieJar!!)
                } else if (config!!.enableTempCookieJar) {
                    builder.cookieJar(TempCookieJar.getInstance() as CookieJar)
                }

                client = builder.build()
            }
            builder = OkHttpClient.Builder() //client!!.newBuilder()
            builder.connectTimeout(config!!.connectTimeout.toLong(), TimeUnit.SECONDS)
            builder.readTimeout(config!!.readTimeout.toLong(), TimeUnit.SECONDS)
            builder.writeTimeout(config!!.writeTimeout.toLong(), TimeUnit.SECONDS)
            if (getDefaultHeaders(this.request!!) != 2 && config!!.headers != null && config!!.headers!!.size > 0) {
                val it: Iterator<*> = config!!.headers!!.iterator()

                while (it.hasNext()) {
                    val header = it.next() as Header
                    addHeader(request, header, false)
                }
            }

            if (config!!.trustAllCerts) {
                builder = enableTrustAllCert(builder)
            }

            if (config!!.hostnameVerifier != null) {
                val hostnameVerifier = config!!.hostnameVerifier
                if (hostnameVerifier != null) {
                    builder.hostnameVerifier(hostnameVerifier)
                }
            }

            this.trustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            if (trustManagerFactory != null) {
                this.trustManagerFactory!!.init(null as KeyStore?)
                val trustManagers = trustManagerFactory!!.trustManagers
                if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                    throw IllegalStateException(
                        "Unexpected default trust managers: ${
                            Arrays.toString(
                                trustManagers
                            )
                        }"
                    )
                }

                val trustManager = trustManagers[0] as X509TrustManager
                val sslContext = SSLContext.getInstance("TLS")
                sslContext.init(null, arrayOf<TrustManager>(trustManager), SecureRandom())
                sslSocketFactory = sslContext.socketFactory
                if (config!!.sslSocketFactory != null) {
                    val sslSocketFactory = config!!.sslSocketFactory
                    if (sslSocketFactory != null) {
                        builder.sslSocketFactory(
                            sslSocketFactory = sslSocketFactory,
                            trustManager = trustManager
                        )
                    }
                }

                if (getSslSocketFactory(request) != null) {
                    builder.sslSocketFactory(sslSocketFactory!!, trustManager = trustManager)
                }
            }

            if (getConnectTimeout(request) > 0) {
                builder.connectTimeout(getConnectTimeout(request) as Long, TimeUnit.SECONDS)
            }

            if (getReadTimeout(request) > 0) {
                builder.readTimeout(getReadTimeout(request) as Long, TimeUnit.SECONDS)
            }

            if (getWriteTimeout(request) > 0) {
                builder.writeTimeout(getWriteTimeout(request) as Long, TimeUnit.SECONDS)
            }

            if (isTrustAllCerts(request)) {
                builder = this.enableTrustAllCert(builder)
            }

            if (getHostnameVerifier(request) != null) {
                builder.hostnameVerifier(getHostnameVerifier(request)!!)
            }

            enableResponseCaching = when (getResponseCachingStatus(request)) {
                0 -> config!!.enableResponseCaching
                1 -> true
                2 -> false
                else -> config!!.enableResponseCaching
            }

            if (getResponseCacheTimeout(request) > -1) {
                this.responseCacheTimeout = getResponseCacheTimeout(request)
            } else {
                this.responseCacheTimeout = config!!.responseCacheTimeout
            }

            debugPrintInfo = when (getDebugPrintInfo(request)) {
                0 -> config!!.debugPrintInfo
                1 -> true
                2 -> false
                else -> config!!.debugPrintInfo
            }

            debugPrintHeaders = when (getDebugPrintHeaders(request)) {
                0 -> config!!.debugPrintHeaders
                1 -> true
                2 -> false
                else -> config!!.debugPrintHeaders
            }

            debugPrintTimes = when (getDebugPrintTimes(request)) {
                0 -> config!!.debugPrintTimes
                1 -> true
                2 -> false
                else -> config!!.debugPrintTimes
            }

            this.forceGzipDecode = isForceGzipDecode(request)
            client = builder.build()
            this.requestId = dateFormatRequestId.format(Date()) + getRequestCount()
            if (this.debugPrintInfo) {
                this.log("RestClient initialized... Version" + context.getString(R.string.version))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setConfig(config: RestClientConfig) {
        this.config = config
    }

    fun getRequestCount(): Int {
        ++requestCount
        return requestCount
    }

    private fun enableTrustAllCert(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        try {
            val trustManager = arrayOf<TrustManager>(@SuppressLint("CustomX509TrustManager")
            object : X509TrustManager {
                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<X509Certificate>,
                    authType: String,
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate?> {
                    return arrayOfNulls(0)
                }
            })
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustManager, SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            builder.sslSocketFactory(
                sslSocketFactory = sslSocketFactory,
                trustManager = trustManager[1] as X509TrustManager
            )
            builder.hostnameVerifier(HostnameVerifier { hostname, session -> true })
        } catch (t: Throwable) {
            t.printStackTrace()
        }
        return builder
    }

    fun checkConnected(context: Context): Boolean {
        try {
            val cm: ConnectivityManager =
                context.getSystemService("connectivity") as ConnectivityManager
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (t: Throwable) {
            return false
        }
    }

    fun getCacheForRequest(context: Context, request: Request): CacheData? {
        try {
            var method: String? = null
            when (getMethod(request)) {
                GET -> method = "get"
                POST -> method = "post"
                PUT -> method = "put"
                DELETE -> method = "delete"
                else -> null
            }
            if (method != null) {
                val cacheData: CacheData? = getInstance(context)
                    .getCacheData(getHttpUrl(request).toString(), method = method)
                if (cacheData != null) {
                    val cacheAge: Long = System.currentTimeMillis() - cacheData.updatedAt
                    when {
                        cacheData.timeout <= 0 -> cacheData.status = false
                        cacheAge < cacheData.timeout.toLong() -> cacheData.status = false
                        else -> cacheData.status = true
                    }
                    return cacheData
                } else {
                    return null
                }
            } else {
                return null
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            return null
        }
    }

    fun deleteCacheForRequest(context: Context, request: Request): Boolean {
        try {
            var method: String? = null
            when (getMethod(request)) {
                GET -> method = "get"
                POST -> method = "post"
                PUT -> method = "put"
                DELETE -> method = "delete"
                else -> null
            }
            val result: Int = getInstance(context).deleteCacheData(
                getHttpUrl(request).toString(),
                method!!
            )
            return true
        } catch (t: Throwable) {
            t.printStackTrace()
            return false
        }
    }

    fun getCache(context: Context, request: Request): CacheData? {
        try {
            val cacheData: CacheData? = getCacheForRequest(context, request)
            if (cacheData != null) {
                this.log("Cache Data:" + cacheData.data)
                return cacheData
            } else {
                return null
            }
        } catch (t: Throwable) {
            t.printStackTrace()
            return null
        }
    }

    private fun log(message: String) {
        Log.d("RestClientKot24:ReqId=" + this.requestId, message)
        if (this.logDirectory != null) {
            if (!logDirectory!!.exists()) {
                logDirectory!!.mkdirs()
            } else if (!logDirectory!!.isDirectory) {
                return
            }
        }

        val date = Date()
        val fileName: String = "RestClient-" + dateFormatFileName.format(date) + ".txt"
        val logEntryTime: String = dateFormatLogEntry.format(date)
        val logFile = File(logDirectory, fileName)
        if (!logFile.exists()) {
            try {
                logFile.createNewFile()
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        try {
            val writer = BufferedWriter(FileWriter(logFile, true))
            writer.append(logEntryTime)
                .append("ReqId:")
                .append(requestId)
                .append(": ")
                .append(message)
            writer.newLine()
            writer.close()
        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }

    fun execute(): Call? {
        val response: Response = Response()
        response.setRequest(request)
        if (enableResponseCaching) {
            try {
                if (isCacheCallbackEnabled(request)!!) {
                    val cacheData: CacheData = getCache(context!!, request!!)!!
                    if (cacheData != null && !cacheData.status && callback != null) {
                        val cacheResponse: Response = Response()
                        cacheResponse.setCacheData(cacheData)
                        cacheResponse.setResponseBody(cacheData.data)
                        callback!!.onResponseReceive(cacheResponse)
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }

        if (checkConnected(context!!)) {
            try {
                val builder: okhttp3.Request.Builder = okhttp3.Request.Builder()
                builder.url(getHttpUrl(request!!)!!)

                val headerList: MutableList<Header> = getHeaders(request!!)!!
                for (listItem in headerList) {
                    val header: Header = listItem
                    builder.addHeader(name = header.name, value = header.value)
                }
                var processedBodyType: Request.BodyType = getBodyType(request!!)!!
                if (getMethod(request!!)!! == GET) {
                    builder.get()
                } else {
                    val body: RequestBody
                    val mediaType: MediaType
                    when (getMethod(request!!)!!) {
                        POST -> {
                            when (getBodyType(request!!)!!) {
                                Request.BodyType.JSON -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/json; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.post(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.post(body)
                                    }
                                }

                                Request.BodyType.XML -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/xml; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.post(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.post(body)
                                    }
                                }

                                Request.BodyType.TEXT -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "text/plain; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.post(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.post(body)
                                    }
                                }

                                Request.BodyType.MULTIPART -> {
                                    builder.post(getMultipartBody(request!!)!!)
                                }

                                Request.BodyType.X_FORM_URL_ENCODED -> {
                                    builder.post(getFormBody(request!!)!!)
                                }

                                else -> {
                                    processedBodyType = Request.BodyType.EMPTY
                                    body = ByteArray(0).toRequestBody(null, 0, 0)
                                    builder.post(body)
                                }
                            }
                        }

                        PUT -> {
                            when (getBodyType(request!!)!!) {
                                Request.BodyType.JSON -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/json; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.put(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.put(body)
                                    }
                                }

                                Request.BodyType.XML -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/xml; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.put(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.put(body)
                                    }
                                }

                                Request.BodyType.TEXT -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "text/plain; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.put(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.put(body)
                                    }
                                }

                                Request.BodyType.MULTIPART -> {
                                    builder.put(getMultipartBody(request!!)!!)
                                }

                                Request.BodyType.X_FORM_URL_ENCODED -> {
                                    builder.put(getFormBody(request!!)!!)
                                }

                                else -> {
                                    processedBodyType = Request.BodyType.EMPTY
                                    body = ByteArray(0).toRequestBody(null, 0, 0)
                                    builder.put(body)
                                }
                            }
                        }

                        DELETE -> {
                            when (getBodyType(request!!)!!) {
                                Request.BodyType.JSON -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/json; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.delete(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.delete(body)
                                    }
                                }

                                Request.BodyType.XML -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/xml; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.delete(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.delete(body)
                                    }
                                }

                                Request.BodyType.TEXT -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "text/plain; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.delete(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.delete(body)
                                    }
                                }

                                Request.BodyType.MULTIPART -> {
                                    builder.delete(getMultipartBody(request!!)!!)
                                }

                                Request.BodyType.X_FORM_URL_ENCODED -> {
                                    builder.delete(getFormBody(request!!)!!)
                                }

                                else -> {
                                    processedBodyType = Request.BodyType.EMPTY
                                    body = ByteArray(0).toRequestBody(null, 0, 0)
                                    builder.delete(body)
                                }
                            }
                        }

                        GET -> {
                            TODO()
                        }

                        PATCH -> {
                            when (getBodyType(request!!)!!) {
                                Request.BodyType.JSON -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/json; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.patch(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.patch(body)
                                    }
                                }

                                Request.BodyType.XML -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "application/xml; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.patch(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.patch(body)
                                    }
                                }

                                Request.BodyType.TEXT -> {
                                    if (getRawBody(request!!) != null && getRawBody(request!!)!!.isNotEmpty()) {
                                        mediaType =
                                            "text/plain; charset=utf-8".toMediaTypeOrNull()!!
                                        body = getRawBody(request!!)!!.toRequestBody(mediaType)
                                        builder.patch(body)
                                    } else {
                                        processedBodyType = Request.BodyType.EMPTY
                                        body = ByteArray(0).toRequestBody(null, 0, 0)
                                        builder.patch(body)
                                    }
                                }

                                Request.BodyType.MULTIPART -> {
                                    builder.patch(getMultipartBody(request!!)!!)
                                }

                                Request.BodyType.X_FORM_URL_ENCODED -> {
                                    builder.patch(getFormBody(request!!)!!)
                                }

                                else -> {
                                    processedBodyType = Request.BodyType.EMPTY
                                    body = ByteArray(0).toRequestBody(null, 0, 0)
                                    builder.patch(body)
                                }
                            }
                        }
                    }
                }

                val httpRequest: okhttp3.Request = builder.build()
                if (debugPrintInfo) {
                    val method: String = httpRequest.method
                    this.log("Request: " + method + " " + httpRequest.url.toString())
                    if ((method.equals("post", true)) or
                        (method.equals("put", true)) or
                        (method.equals("patch", true)) or
                        (method.equals("delete", true))
                    ) {
                        var count: Int
                        var body: String
                        var i: Int
                        when (processedBodyType) {
                            Request.BodyType.EMPTY -> log("Request Body: Empty")
                            Request.BodyType.X_FORM_URL_ENCODED -> {
                                try {
                                    count = getFormBody(request!!)!!.size
                                    body = ""
                                    i = 0
                                    while (i < count) {
                                        body = (body + getFormBody(request!!)!!
                                            .name(i)) + " : " + getFormBody(request!!)!!
                                            .value(i)
                                        ++i
                                    }
                                    log("Request Body: Form body : " + body.trim())
                                } catch (t: Throwable) {
                                    log("Request Body: Form body")
                                    t.printStackTrace()
                                }
                            }

                            Request.BodyType.MULTIPART -> {
                                try {
                                    count = getMultipartBody(request!!)!!.parts.size
                                    body = ""
                                    i = 0
                                    while (i < count) {
                                        body = (body + getMultipartBody(request!!)!!.part(i)
                                                + " : " + getMultipartBody(request!!)!!.part(i))
                                        ++i
                                    }
                                    log("Request Body: Multipart Body : " + body.trim())
                                } catch (t: Throwable) {
                                    log("Request Body: Multipart Body")
                                    t.printStackTrace()
                                }
                            }

                            else -> {
                                log("Request Body: " + getRawBody(request!!)!!)
                            }
                        }

                    }
                }
                if (debugPrintHeaders) {
                    log("Request Headers: " + httpRequest.headers.toMultimap().toString())
                }
                call = this.client?.newCall(httpRequest)
                executionStart = System.currentTimeMillis()
                call!!.enqueue(object : Callback {
                    private var call: Call? = null
                    private var httpResponse: okhttp3.Response? = null
                    var handler: Handler? = null

                    init {
                        this.handler = Handler(context!!.mainLooper)
                    }

                    override fun onFailure(call: Call, exception: IOException) {
                        log("Failed: $exception")
                        if (debugPrintTimes) {
                            val executionTime =
                                System.currentTimeMillis() - executionStart
                            log("Execution Time: " + executionTime + "ms")
                        }

                        val rex: RestClientException
                        when (exception) {
                            is SocketTimeoutException -> {
                                rex = ConnectionException(-6, "Socket timeout", exception)
                                response.setException(rex)
                                response.setCall(call)
                            }

                            is SocketException -> {
                                rex = ConnectionException(-10, exception.message!!, exception)
                                response.setException(rex)
                                response.setCall(call)
                            }

                            is UnknownHostException -> {
                                rex = ConnectionException(
                                    -7,
                                    "Unknown host. " + exception.message,
                                    exception
                                )
                                response.setException(rex)
                                response.setCall(call)
                            }

                            is MalformedURLException -> {
                                rex = ConnectionException(-8, exception.message!!, exception)
                                response.setException(rex)
                                response.setCall(call)
                            }

                            is ConnectException -> {
                                rex = ConnectionException(-9, exception.message!!, exception)
                                response.setException(rex)
                                response.setCall(call)
                            }

                            else -> {
                                rex = RestClientException(-1, exception.message!!, exception)
                                response.setException(rex)
                                response.setCall(call)
                            }
                        }
                        if (debugPrintInfo) {
                            log("Failed: " + rex.message)
                        }

                        handler!!.post {
                            if (callback != null) {
                                callback!!.onResponseReceive(response)
                            }
                        }
                    }

                    @Throws(IOException::class)
                    override fun onResponse(call: Call, httpResponse: okhttp3.Response) {
                        this.call = call
                        this.httpResponse = httpResponse
                        if (debugPrintTimes) {
                            val executionTime =
                                System.currentTimeMillis() - executionStart
                            log("Execution Time: " + executionTime + "ms")
                        }

                        if (forceGzipDecode) {
                            val gzipInputStream = GZIPInputStream(
                                httpResponse.body!!.byteStream()
                            )
                            val reader = InputStreamReader(gzipInputStream)
                            val br = BufferedReader(reader)
                            val builder = StringBuilder()

                            var inputLine: String?
                            while ((br.readLine().also { inputLine = it }) != null) {
                                builder.append(inputLine)
                            }

                            response.setResponseBody(builder.toString())
                        } else {
                            try {
                                response.setResponseBody(httpResponse.body!!.string())
                            } catch (var8: java.lang.Exception) {
                                response.setResponseBody(null)
                            }
                        }

                        response.setResponse(httpResponse)
                        response.setCall(call)
                        if (debugPrintInfo) {
                            log(("Response Code: " + response.getStatusCode()).toString() + " " + response.getStatusMessage())
                        }

                        if (debugPrintHeaders) {
                            log("Response Headers: " + response.getHeaders().toString())
                        }

                        if (debugPrintInfo) {
                            log("Response Body: " + response.getResponseBody())
                        }

                        try {
                            if (enableResponseCaching && response.isSuccessful() && (response.getResponseBody()
                                    .isNotEmpty())
                            ) {
                                val method: String = when (getMethod(request!!)) {
                                    GET -> "get"
                                    POST -> "post"
                                    DELETE -> "delete"
                                    PUT -> "put"
                                    PATCH -> "patch"
                                    null -> null.toString()
                                }
                                val cacheData = CacheData(
                                    url = getHttpUrl(request!!).toString(),
                                    method = method,
                                    data = response.getResponseBody(),
                                    timeout = responseCacheTimeout,
                                    createdAt = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                val result: Int = getInstance(context).addCacheEntry(cacheData)
                                if (debugPrintInfo) {
                                    log("Response Cache Saved: $result")
                                }
                            }
                        } catch (t: Throwable) {
                            if (debugPrintInfo) {
                                log("Response Cache Error: " + t.message)
                            }
                        }

                        handler!!.post {
                            if (callback != null) {
                                callback!!.onResponseReceive(response)
                            }
                        }
                    }
                })
            } catch (t: Throwable) {
                t.printStackTrace()
                response.setException(RestClientException(-1, t.message.toString(), t))
                callback?.onResponseReceive(response)
            }
        } else {
            if (debugPrintInfo) {
                log("Not connected to internet")
            }
            val exception: RestClientException =
                ConnectionException(-4, "Not connected to internet")
            response.setException(exception = exception)
            callback?.onResponseReceive(response)
        }
        return this.call
    }
}