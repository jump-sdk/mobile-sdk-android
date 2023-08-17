package com.spreedly.client.models

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

class ApplePayInfo(
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
        request["apple_pay"] = JsonObject(
            mapOf("payment_data" to Json.decodeFromString(paymentData)),
        )
        return JsonObject(
            mapOf("payment_method" to JsonObject(request)),
        )
    }
}
