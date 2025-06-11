package com.supunishara.restclientkot24.callbacks

import com.supunishara.restclientkot24.Response

/**
 * Lightweight single-callback interface for receiving a completed [Response].
 *
 * Use this when you only care about the final response object regardless of success/failure.
 */
abstract class ResponseCallbacks {

    /**
     * Called when the response is returned from the request,
     * either successful, failed, or from cache.
     */
    abstract fun onResponseReceive(response: Response)
}
