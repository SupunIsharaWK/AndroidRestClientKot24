package com.supunishara.restclientkot24.data_classes

import org.junit.Test
import org.junit.Assert.*

class HeaderTest {

    @Test
    fun headersWithSameNameAreEqualIgnoringCase() {
        val h1 = Header("Authorization", "Token")
        val h2 = Header("authorization", "Token")
        assertEquals(h1, h2)
    }

    @Test
    fun headersWithDifferentNamesAreNotEqual() {
        val h1 = Header("Accept", "application/json")
        val h2 = Header("Content-Type", "application/json")
        assertNotEquals(h1, h2)
    }

    @Test
    fun hashCodesMatchForSameNamesIgnoringCase() {
        val h1 = Header("X-Test", "abc")
        val h2 = Header("x-test", "abc")
        assertEquals(h1.hashCode(), h2.hashCode())
    }

    @Test
    fun unequalHeadersProduceDifferentHashCodes() {
        val h1 = Header("X-Test-A", "abc")
        val h2 = Header("X-Test-B", "abc")
        assertNotEquals(h1.hashCode(), h2.hashCode())
    }


    @Test
    fun testHeaderEqualityCaseInsensitiveName() {
        val header1 = Header("Content-Type", "application/json")
        val header2 = Header("content-type", "application/json")

        assertEquals(header1, header2)
        assertEquals(header1.hashCode(), header2.hashCode())
    }

    @Test
    fun testHeaderInequalityDifferentNames() {
        val header1 = Header("Authorization", "Bearer abc123")
        val header2 = Header("Content-Type", "application/json")

        assertNotEquals(header1, header2)
    }

    @Test
    fun testHeaderHashCodeUniqueness() {
        val header1 = Header("X-Test", "1")
        val header2 = Header("X-Test", "2")

        assertNotEquals(header1.hashCode(), header2.hashCode())
    }

    @Test
    fun testToStringNotEmpty() {
        val header = Header("Accept", "application/json")
        assertNotNull(header.toString())
    }
}
