package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplePayInfoTests {
    @Test
    fun CanCreateApplePayInfo() {
        val applePay = ApplePayInfo("Jane", "Doe", "sample data")
        assertTrue(applePay.firstName === "Jane" && applePay.lastName === "Doe" && applePay.paymentData === "sample data")
    }

    @Test
    fun CanEncodeApplePay() {
        val applePay = ApplePayInfo("Jane", "Doe", "sample data")
        applePay.testCardNumber = "111111111111111"
        val expected =
            "{\"payment_method\":{\"apple_pay\":{\"test_card_number\":\"111111111111111\",\"last_name\":\"Doe\",\"payment_data\":\"sample data\",\"first_name\":\"Jane\"}}}"
        val actual = applePay.toJson()
        assertEquals(expected, actual.toString())
    }
}
