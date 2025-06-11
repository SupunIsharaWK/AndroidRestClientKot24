package com.supunishara.restclientkot24.utils

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * Singleton object responsible for providing a preconfigured instance of [Gson].
 *
 * This centralizes Gson customization in one place — such as date formatting,
 * field naming policies, null serialization, etc. You can inject or access this
 * wherever JSON parsing is needed.
 */
object GsonProvider {

    /**
     * Lazily initialized Gson instance with default settings.
     *
     * - Serializes nulls to maintain field presence
     * - Enables pretty printing for readability (useful for logging/debugging)
     * - You can customize this further (e.g., date format, type adapters)
     */
    val gson: Gson by lazy {
        GsonBuilder()
            .serializeNulls()         // Include null values in output
            .setPrettyPrinting()      // Enable indented output (mainly for logs)
            .create()
    }
}