package com.supunishara.restclientkot24.exceptions

open class RestClientException : Throwable {
    private var errorCode: Int = 0

    constructor(code: Int, detailMessage: String) : super(detailMessage)
    constructor(code: Int, detailMessage: String, throwable: Throwable) : super(
        detailMessage,
        throwable
    ) {
        this.errorCode = code
    }

    fun getErrorCode(): Int {
        return this.errorCode
    }
}