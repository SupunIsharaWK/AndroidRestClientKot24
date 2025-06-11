package com.supunishara.restclientkot24.security

/**
 * Interface for providing auth tokens dynamically.
 * Implement this in the app that uses the AAR.
 */
interface AuthTokenProvider {
    fun getToken(): String?
}