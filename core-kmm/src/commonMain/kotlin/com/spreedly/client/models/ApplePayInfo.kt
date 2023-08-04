package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class ApplePayInfo(
    firstName: String,
    lastName: String,
    val paymentData: String,
    retained: Boolean? = null,
) : PaymentMethodInfo(
    firstName = firstName,
    lastName = lastName,
    retained = retained,
) {
    var testCardNumber: String? = null

    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val applePay = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, applePay)
        applePay.putAsJsonElement("payment_data", paymentData)
        applePay.putAsJsonElement("test_card_number", testCardNumber)
        paymentMethod.put("apple_pay", JsonObject(applePay))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
