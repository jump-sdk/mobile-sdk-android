package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GooglePayInfoTest {
    @Test
    fun CanCreateGooglePayInfo() {
        val googlePay = GooglePayInfo("Jane", "Doe", "sample data", false)
        assertTrue(googlePay.firstName === "Jane" && googlePay.lastName === "Doe" && googlePay.paymentData === "sample data" && googlePay.retained == false)
    }

    @Test
    fun CanEncodeGooglePay() {
        val googlePay = GooglePayInfo("Jane", "Doe", "sample data", false)
        googlePay.testCardNumber = "111111111111111"
        val expected =
            "{\"payment_method\":{\"retained\":false,\"google_pay\":{\"test_card_number\":\"111111111111111\",\"last_name\":\"Doe\",\"payment_data\":\"sample data\",\"first_name\":\"Jane\"}}}"
        val actual = googlePay.toJson()
        assertEquals(expected, actual.toString())
    }
}
