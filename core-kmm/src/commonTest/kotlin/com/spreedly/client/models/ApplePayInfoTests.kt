package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertContains

class ApplePayInfoTests {
    @Test
    fun CanEncodeApplePay() {
        val applePay = ApplePayInfo("Jane", "Doe", "sample data")
        applePay.testCardNumber = "111111111111111"

        val json = applePay.toJson().toString()
        assertContains(json, "\"payment_method\":{\"apple_pay\"")
        assertContains(json, "\"first_name\":\"Jane\"")
        assertContains(json, "\"last_name\":\"Doe\"")
        assertContains(json, "\"payment_data\":\"sample data\"")
        assertContains(json, "\"test_card_number\":\"111111111111111\"")
    }
}
