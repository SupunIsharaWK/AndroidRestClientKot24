package com.supunishara.restclientkot24.callbacks

import com.supunishara.restclientkot24.Response

/**
 * Abstract callback interface for advanced request response handling.
 *
 * This is suitable when you want granular control over different outcomes:
 * - Success
 * - Failure (e.g., network issues)
 * - Cache fallback
 * - Retry behavior
 */
abstract class MultiResponseCallback {

    /** Called when a successful HTTP response is received. */
    abstract fun onSuccess(response: Response)

    /** Called when a request fails due to an exception (timeout, network, etc). */
    abstract fun onFailure(exception: Throwable)

    /** Called if a cached response is served instead of a live request. */
    open fun onCacheHit(response: Response) {}

    /** Called if the request logic supports and triggers a retry. */
    open fun onRetry(attempt: Int) {}
}
