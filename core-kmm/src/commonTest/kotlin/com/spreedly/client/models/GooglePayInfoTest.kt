package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertContains

class GooglePayInfoTest {
    @Test
    fun CanEncodeGooglePay() {
        val googlePay = GooglePayInfo(
            firstName = "Jane",
            lastName = "Doe",
            paymentData = "sample data",
            retained = false,
        )
        googlePay.testCardNumber = "111111111111111"

        val json = googlePay.toJson().toString()
        assertContains(json, "\"google_pay\"")
        assertContains(json, "\"retained\":false")
        assertContains(json, "\"first_name\":\"Jane\"")
        assertContains(json, "\"last_name\":\"Doe\"")
        assertContains(json, "\"payment_data\":\"sample data\"")
        assertContains(json, "\"test_card_number\":\"111111111111111\"")
    }
}
