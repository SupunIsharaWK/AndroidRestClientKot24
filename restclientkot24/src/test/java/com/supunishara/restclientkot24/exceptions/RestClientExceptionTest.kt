package com.supunishara.restclientkot24.exceptions

import com.supunishara.restclientkot24.exceptions.RestClientException
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [28],
    manifest = Config.NONE
)  //@Config(sdk = [33]) // or any API level you want to simulate
class RestClientExceptionTest {

    @Test
    fun testMessageAndCode() {
        val exception = RestClientException(-1, "Something went wrong")
        assertEquals("Something went wrong", exception.message)
        assertEquals(-1, exception.errorCode)
    }

    @Test
    fun testMessageCodeAndCause() {
        val cause = IllegalStateException("Illegal state")
        val exception = RestClientException(-2, "Caused failure", cause)
        assertEquals("Caused failure", exception.message)
        assertEquals(-2, exception.errorCode)
        assertEquals(cause, exception.cause)
    }
}
