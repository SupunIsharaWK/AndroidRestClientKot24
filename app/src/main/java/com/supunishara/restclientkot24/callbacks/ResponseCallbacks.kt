package com.supunishara.restclientkot24.callbacks

import com.supunishara.restclientkot24.Response

abstract class ResponseCallbacks {
    abstract fun onResponseReceive(response: Response)
}