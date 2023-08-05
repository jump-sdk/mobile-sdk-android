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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as GooglePayInfo

        if (paymentData != other.paymentData) return false
        if (testCardNumber != other.testCardNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = paymentData.hashCode()
        result = 31 * result + (testCardNumber?.hashCode() ?: 0)
        return result
    }
}
