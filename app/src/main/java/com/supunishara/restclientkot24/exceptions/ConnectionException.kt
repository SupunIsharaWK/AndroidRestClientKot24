package com.supunishara.restclientkot24.exceptions

import com.supunishara.restclientkot24.exceptions.RestClientException as RestClientException

class ConnectionException : RestClientException {
    constructor(code: Int, detailMessage: String) : super(code, detailMessage)
    constructor(code: Int, detailMessage: String, throwable: Throwable) : super(
        code,
        detailMessage,
        throwable
    )

}