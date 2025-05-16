package com.supunishara.restclientkot24

import com.supunishara.restclientkot24.Request

import com.supunishara.restclientkot24.data_classes.CacheData
import com.supunishara.restclientkot24.exceptions.RestClientException
import okhttp3.Call
import okhttp3.Response

class Response {
    private var request: Request? = null
    private var responseBody = ""
    private var responseCode = 0
    private var httpResponse: Response? = null
    private var call: Call? = null
    private var exception: RestClientException? = null
    private var cacheData: CacheData? = null

    constructor()

    constructor(
        request: Request?,
        responseBody: String?,
        httpResponse: Response?,
        call: Call?,
        exception: RestClientException?,
        cacheData: CacheData?
    ) {
        this.request = request
        this.responseBody = responseBody ?: ""
        this.httpResponse = httpResponse
        this.call = call
        this.exception = exception
        this.cacheData = cacheData
    }

    constructor(
        request: Request?,
        responseBody: String?,
        httpResponse: Response?,
        call: Call?,
        exception: RestClientException?
    ) {
        this.request = request
        this.responseBody = responseBody ?: ""
        this.httpResponse = httpResponse
        this.call = call
        this.exception = exception
    }

    fun getRequest(): Request? {
        return this.request
    }

    fun setRequest(request: Request?) {
        this.request = request
    }

    fun getResponseBody(): String {
        return this.responseBody
    }

    fun setResponseBody(responseBody: String?) {
        if (responseBody != null) {
            this.responseBody = responseBody
        }
    }

    fun getResponse(): Response? {
        return this.httpResponse
    }

    fun setResponse(httpResponse: Response?) {
        this.httpResponse = httpResponse
    }

    fun getCall(): Call? {
        return this.call
    }

    fun setCall(call: Call?) {
        this.call = call
    }

    fun getException(): Throwable? {
        return this.exception
    }

    fun getStatusCode(): Int {
        return if (this.exception == null) {
            if (this.httpResponse != null) httpResponse!!.code else 0
        } else {
            exception!!.getErrorCode()
        }
    }

    fun getStatusMessage(): String {
        if (this.exception == null) {
            if (this.httpResponse != null) {
                return httpResponse!!.message
            } else {
                return "Unknown response"
            }
        } else {
            return exception.toString()
        }
    }

    fun getHeaders(): Map<String, List<String>>? {
        return try {
            httpResponse!!.headers.toMultimap()
        } catch (var2: java.lang.Exception) {
            null
        }
    }

    fun getHeaders(name: String?): List<String>? {
        return try {
            httpResponse!!.headers.values(name!!)
        } catch (var3: java.lang.Exception) {
            null
        }
    }

    private fun getCacheResponse(): String {
        return try {
            if (this.cacheData == null) "" else cacheData!!.data
        } catch (var2: java.lang.Exception) {
            ""
        }
    }

    fun getCacheData(): CacheData? {
        return this.cacheData
    }

    fun setCacheData(cacheData: CacheData?) {
        this.cacheData = cacheData
    }

    fun isSuccessful(): Boolean {
        return if (this.httpResponse != null) {
            httpResponse!!.isSuccessful
        } else {
            cacheData != null
        }
    }

    fun isExecuted(): Boolean {
        return if (this.call == null) false else call!!.isExecuted()
    }

    fun isException(): Boolean {
        return this.exception != null
    }

    fun setException(exception: RestClientException?) {
        this.exception = exception
    }

    fun isCache(): Boolean {
        return try {
            cacheData != null
        } catch (var2: java.lang.Exception) {
            false
        }
    }

    override fun toString(): String {
        return this.getResponseBody()
    }
}