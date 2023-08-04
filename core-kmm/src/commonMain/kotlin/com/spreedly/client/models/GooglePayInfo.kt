package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class GooglePayInfo(
    firstName: String,
    lastName: String,
    val paymentData: String,
    retained: Boolean
) : PaymentMethodInfo(
    firstName = firstName,
    lastName = lastName,
    retained = retained,
) {
    var testCardNumber: String? = null

    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val googlePay = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, googlePay)
        googlePay.putAsJsonElement("payment_data", paymentData)
        googlePay.putAsJsonElement("test_card_number", testCardNumber)
        paymentMethod.put("google_pay", JsonObject(googlePay))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
