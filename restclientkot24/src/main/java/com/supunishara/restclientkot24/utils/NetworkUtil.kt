package com.supunishara.restclientkot24.utils

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.M

/**
 * Utility object for checking internet connectivity status.
 * This is used to ensure network availability before making network requests.
 */
object NetworkUtil {
    /**
     * Checks whether the device has an active internet connection.
     *
     * @param context Context required to access system services.
     * @return true if the device is connected to the internet; false otherwise.
     */
    @SuppressLint("ObsoleteSdkInt")
    fun isConnected(context: Context?): Boolean {
        // Get the system's ConnectivityManager
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
                ?: return false

        return if (SDK_INT >= M) {
            // For Android Marshmallow (API 23) and above, use NetworkCapabilities
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            // Check if the network has internet capability
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            // For older Android versions, use the deprecated activeNetworkInfo
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo != null && networkInfo.isConnected
        }
    }
}