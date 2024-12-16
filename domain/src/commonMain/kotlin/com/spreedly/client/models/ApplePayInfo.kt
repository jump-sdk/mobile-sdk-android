package com.spreedly.client.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class ApplePayInfo(
    val testCardNumber: String? = null,
    firstName: String?,
    lastName: String?,
    email: String?,
    val paymentData: String,
) : PaymentMethodInfo(
    fullName = null,
    firstName = firstName,
    lastName = lastName,
    email = email,
    retained = false,
) {
    override fun toJson(): JsonObject {
        // The Apple Pay endpoint expects the person-specific and address
        // information up at the payment_method level unlike the other
        // types of payment methods.
        val request = (generateBaseRequestMap() + generatePersonInfoMap()).toMutableMap()
        request["apple_pay"] = JsonObject(buildMap {
            put("payment_data", Json.decodeFromString(paymentData))
            testCardNumber?.let { put("test_card_number", Json.decodeFromString(it)) }
        })
        return JsonObject(
            mapOf("payment_method" to JsonObject(request)),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as ApplePayInfo

        return paymentData == other.paymentData
    }

    override fun hashCode(): Int = paymentData.hashCode()
}
