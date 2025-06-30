package com.supunishara.restclientkot24.exceptions

import com.supunishara.restclientkot24.exceptions.ConnectionException
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import kotlin.intArrayOf

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [28],
    manifest = Config.NONE
)  //@Config(sdk = [33]) // or any API level you want to simulate
class ConnectionExceptionTest {

    @Test
    fun testBasicConstructor() {
        val exception = ConnectionException(-4, "Connection failed")
        assertEquals("Connection failed", exception.message)
        assertEquals(-4, exception.errorCode)
    }

    @Test
    fun testConstructorWithCause() {
        val cause = IOException("Timeout")
        val exception = ConnectionException(-6, "Socket timeout", cause)

        assertEquals("Socket timeout", exception.message)
        assertEquals(-6, exception.errorCode)
        assertEquals(cause, exception.cause)
    }
}
