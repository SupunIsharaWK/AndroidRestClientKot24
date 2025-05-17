package com.supunishara.restclientkot24

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.util.Log
import com.supunishara.restclientkot24.callbacks.MultiResponseCallback
import com.supunishara.restclientkot24.callbacks.ProgressCallbacks
import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.ConnectionException
import com.supunishara.restclientkot24.exceptions.RestClientException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.net.*
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPInputStream
import javax.net.ssl.X509TrustManager

/**
 * A reusable HTTP client wrapper built on OkHttp, designed to support:
 * - Fluent Request object
 * - Callbacks and coroutines
 * - Caching and debugging
 */
class RestClient(private val context: Context?) {

    private var clientConfig: RestClientConfig? = null
    var client: OkHttpClient? = null
    private var requestCount: Int = 0

    /**
     * Assign a custom client configuration.
     */
    fun setConfig(config: RestClientConfig) {
        this.clientConfig = config
    }

    /**
     * Returns number of executed requests.
     */
    fun getRequestCount(): Int = requestCount

    /**
     * Executes a request using any of the provided callback interfaces:
     * - [MultiResponseCallback] for lifecycle events
     * - [ProgressCallbacks] for UI progress tracking
     * - Lambda for simple `Response` consumption
     */
    fun executeRequest(
        request: Request,
        multiCallback: MultiResponseCallback? = null,
        progressCallback: ProgressCallbacks? = null,
        simpleCallback: ((Response) -> Unit)? = null
    ): Call? {
        val response = Response().apply { this.request = request }

        progressCallback?.onCallStart(true)

        // check persistent cache before making a network request
        if (request.responseCachingStatus == 1) {
            val cached = CacheManager.getCacheEntry(
                context!!,
                request.method.name.lowercase(),
                request.httpUrl.toString()
            )
            if (cached != null) {
                val cachedResponse = Response().apply {
                    this.request = request
                    this.cacheData = cached
                    this.responseBody = cached.data
                }

                progressCallback?.onCallStart(false)
                multiCallback?.onCacheHit(cachedResponse)
                simpleCallback?.invoke(cachedResponse)
                return null
            }
        }

        if (!isConnected()) {
            val exception =
                ConnectionException(Result.NOT_CONNECTED_TO_INTERNET, "Not connected to internet")
            response.exception = exception
            progressCallback?.onCallStart(false)
            multiCallback?.onFailure(exception)
            simpleCallback?.invoke(response)
            return null
        }

        return try {
            requestCount++
            val builder = okhttp3.Request.Builder().url(request.httpUrl!!)
            val body = buildRequestBody(request)

            when (request.method) {
                Request.Method.GET -> builder.get()
                Request.Method.DELETE -> builder.delete()
                Request.Method.POST -> builder.post(body)
                Request.Method.PUT -> builder.put(body)
                Request.Method.PATCH -> builder.patch(body)
            }

            request.getHeaders().forEach { builder.addHeader(it.name, it.value) }

            val okHttpRequest = builder.build()
            logRequestInfo(okHttpRequest, request)

            val actualClient = client ?: buildClient(request)
            val call = actualClient.newCall(okHttpRequest)
            val handler = Handler(context!!.mainLooper)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val exception = mapException(e)
                    response.call = call
                    response.exception = exception

                    log("Failure: ${e.message}")

                    handler.post {
                        progressCallback?.onCallStart(false)
                        multiCallback?.onFailure(exception)
                        simpleCallback?.invoke(response)
                    }
                }

                override fun onResponse(call: Call, httpResponse: okhttp3.Response) {
                    response.call = call
                    response.httpResponse = httpResponse
                    response.responseBody = extractBody(httpResponse, request)

                    if (request.responseCachingStatus == 1 && response.isSuccessful && response.responseBody.isNotEmpty()) {
                        val cacheData = CacheData(
                            url = request.httpUrl.toString(),
                            method = request.method.name.lowercase(),
                            data = response.responseBody,
                            timeout = request.responseCacheTimeout,
                            createdAt = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        CacheManager.addCacheEntry(context, cacheData)
                        response.cacheData = cacheData
                    }

                    logResponseInfo(response)

                    handler.post {
                        progressCallback?.onCallStart(false)
                        multiCallback?.onSuccess(response)
                        simpleCallback?.invoke(response)
                    }
                }
            })

            call

        } catch (e: Throwable) {
            val exception = RestClientException(Result.GENERIC_EXCEPTION, e.message ?: "Unknown", e)
            response.exception = exception
            log("Exception during execution: ${e.message}")
            progressCallback?.onCallStart(false)
            multiCallback?.onFailure(exception)
            simpleCallback?.invoke(response)
            null
        }
    }

    /**
     * Coroutine-based version of [executeRequest] with persistent cache support.
     * Use this inside suspend functions or viewModels.
     */
    suspend fun executeRequestSuspend(request: Request): Response = withContext(Dispatchers.IO) {
        // 🔁 Check persistent cache before making a network request (coroutine)
        if (request.responseCachingStatus == 1) {
            val cached = CacheManager.getCacheEntry(context!!, request.method.name.lowercase(), request.httpUrl.toString())
            if (cached != null) {
                return@withContext Response().apply {
                    this.request = request
                    this.cacheData = cached
                    this.responseBody = cached.data
                }
            }
        }

        val response = Response().apply { this.request = request }

        if (!isConnected()) {
            response.exception = ConnectionException(Result.NOT_CONNECTED_TO_INTERNET, "Not connected to internet")
            return@withContext response
        }

        return@withContext try {
            requestCount++
            val builder = okhttp3.Request.Builder().url(request.httpUrl!!)
            val body = buildRequestBody(request)

            when (request.method) {
                Request.Method.GET -> builder.get()
                Request.Method.DELETE -> builder.delete()
                Request.Method.POST -> builder.post(body)
                Request.Method.PUT -> builder.put(body)
                Request.Method.PATCH -> builder.patch(body)
            }

            request.getHeaders().forEach { builder.addHeader(it.name, it.value) }

            val okHttpRequest = builder.build()
            val actualClient = client ?: buildClient(request)

            actualClient.newCall(okHttpRequest).execute().use { httpResponse ->
                response.call = null
                response.httpResponse = httpResponse
                response.responseBody = extractBody(httpResponse, request)

                if (request.responseCachingStatus == 1 && response.isSuccessful && response.responseBody.isNotEmpty()) {
                    val cacheData = CacheData(
                        url = request.httpUrl.toString(),
                        method = request.method.name.lowercase(),
                        data = response.responseBody,
                        timeout = request.responseCacheTimeout,
                        createdAt = System.currentTimeMillis(),
                        updatedAt = System.currentTimeMillis()
                    )
                    CacheManager.addCacheEntry(context!!, cacheData)
                    response.cacheData = cacheData
                }
            }

            response

        } catch (e: Throwable) {
            response.exception = RestClientException(Result.GENERIC_EXCEPTION, e.message ?: "Unknown", e)
            response
        }
    }

    private fun buildClient(request: Request): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .connectTimeout(request.connectTimeout.toLong(), TimeUnit.MILLISECONDS)
            .readTimeout(request.readTimeout.toLong(), TimeUnit.MILLISECONDS)
            .writeTimeout(request.writeTimeout.toLong(), TimeUnit.MILLISECONDS)

        // Include in-memory cookie jar if enabled
        if (clientConfig?.enableTempCookieJar == true) {
            builder.cookieJar(TempCookieJar)
        }

        request.sslSocketFactory?.let { ssl ->
            val trustManager = object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> =
                    arrayOf()

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkClientTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }

                @SuppressLint("TrustAllX509TrustManager")
                override fun checkServerTrusted(
                    chain: Array<java.security.cert.X509Certificate>,
                    authType: String
                ) {
                }
            }
            builder.sslSocketFactory(ssl, trustManager)
        }

        request.hostnameVerifier?.let {
            builder.hostnameVerifier(it)
        }

        return builder.build()
    }

    private fun buildRequestBody(request: Request): RequestBody {
        return when (request.bodyType) {
            Request.BodyType.JSON -> request.rawBody?.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
                ?: EMPTY_BODY

            Request.BodyType.TEXT -> request.rawBody?.toRequestBody("text/plain; charset=utf-8".toMediaTypeOrNull())
                ?: EMPTY_BODY

            Request.BodyType.X_FORM_URL_ENCODED -> request.formBody ?: EMPTY_BODY
            Request.BodyType.MULTIPART -> request.multipartBody ?: EMPTY_BODY
            else -> EMPTY_BODY
        }
    }

    private fun extractBody(httpResponse: okhttp3.Response, request: Request): String {
        return try {
            if (request.forceGzipDecode) {
                GZIPInputStream(httpResponse.body!!.byteStream()).bufferedReader()
                    .use { it.readText() }
            } else {
                httpResponse.body?.string().orEmpty()
            }
        } catch (e: Exception) {
            ""
        }
    }

    private fun mapException(e: IOException): RestClientException {
        return when (e) {
            is SocketTimeoutException -> ConnectionException(Result.SOCKET_TIMEOUT, "Timeout", e)
            is UnknownHostException -> ConnectionException(Result.UNKNOWN_HOST, "Unknown host", e)
            is ConnectException -> ConnectionException(
                Result.CONNECTION_EXCEPTION,
                "Connect failed",
                e
            )

            is MalformedURLException -> ConnectionException(
                Result.MALFORMED_URL,
                "Malformed URL",
                e
            )

            else -> RestClientException(Result.GENERIC_EXCEPTION, e.message ?: "Unknown", e)
        }
    }

    private fun logRequestInfo(request: okhttp3.Request, original: Request) {
        if (original.debugPrintInfo) log("Request: ${request.method} ${request.url}")
        if (original.debugPrintHeaders) log("Headers: ${request.headers.toMultimap()}")
    }

    private fun logResponseInfo(response: Response) {
        if (response.request?.debugPrintInfo == true) {
            log("Response Code: ${response.statusCode} ${response.statusMessage}")
            log("Body: ${response.responseBody}")
        }
        if (response.request?.debugPrintHeaders == true) {
            log("Headers: ${response.headers}")
        }
    }

    private fun isConnected(): Boolean {
        //val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        //return cm?.activeNetworkInfo?.isConnectedOrConnecting == true

        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(network)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true

    }

    private fun log(message: String?) {
        if (!message.isNullOrBlank()) {
            Log.d("RestClient", message)
        }
    }

    companion object {
        private val EMPTY_BODY = ByteArray(0).toRequestBody(null, 0, 0)
    }
}
