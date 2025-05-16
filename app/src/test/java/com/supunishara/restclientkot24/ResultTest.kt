package com.supunishara.restclientkot24

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ResultTest {

    private lateinit var result: Result

    @BeforeEach
    fun setUp() {
        result = Result()
    }

    @AfterEach
    fun tearDown() {
        // Cleanup logic if needed
    }

    @Test
    fun `UNKNOWN constant should be 0`() {
        assertEquals(0, result.UNKNOWN)
    }

    @Test
    fun `GENRIC_EXCEPTION constant should be -1`() {
        assertEquals(-1, result.GENRIC_EXCEPTION)
    }

    @Test
    fun `OKHTTP_LIB_MISSING constant should be -2`() {
        assertEquals(-2, result.OKHTTP_LIB_MISSING)
    }

    @Test
    fun `REQUIRED_PERMISSION_MISSING constant should be -3`() {
        assertEquals(-3, result.REQUIRED_PERMISSION_MISSING)
    }

    @Test
    fun `NOT_CONNECTED_TO_INTERNET constant should be -4`() {
        assertEquals(-4, result.NOT_CONNECTED_TO_INTERNET)
    }

    @Test
    fun `INVALID_PARAMETERS constant should be -5`() {
        assertEquals(-5, result.INVALID_PARAMETERS)
    }

    @Test
    fun `SOCKET_TIMEOUT constant should be -6`() {
        assertEquals(-6, result.SOCKET_TIMEOUT)
    }

    @Test
    fun `UNKNOWN_HOST constant should be -7`() {
        assertEquals(-7, result.UNKNOWN_HOST)
    }

    @Test
    fun `MALFORMED_URL constant should be -8`() {
        assertEquals(-8, result.MALFORMED_URL)
    }

    @Test
    fun `CONNECTION_EXCEPTION constant should be -9`() {
        assertEquals(-9, result.CONNECTION_EXCEPTION)
    }

    @Test
    fun `SOCKET_EXCEPTION constant should be -10`() {
        assertEquals(-10, result.SOCKET_EXCEPTION)
    }
}
