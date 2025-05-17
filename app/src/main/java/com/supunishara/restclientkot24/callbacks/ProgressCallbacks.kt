package com.supunishara.restclientkot24.callbacks

/**
 * Callback interface to track the progress state of an HTTP call.
 *
 * This is typically used to:
 * - Show or hide loading indicators
 * - Disable or re-enable UI elements during a request lifecycle
 */
abstract class ProgressCallbacks {

    /**
     * Called when a request starts or stops loading.
     * @param isLoading `true` when the request starts, `false` when it ends.
     */
    abstract fun onCallStart(isLoading: Boolean)

    /**
     * Called once the request has fully completed (success or failure).
     */
    abstract fun onCallEnd()
}
