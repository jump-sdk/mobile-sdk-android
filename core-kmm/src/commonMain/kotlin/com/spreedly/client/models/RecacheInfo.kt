package com.spreedly.client.models

import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class RecacheInfo(val cvv: SpreedlySecureOpaqueString) {
    fun toJson(): JsonObject {
        val creditCard = JsonObject(
            mapOf("verification_value" to JsonPrimitive(cvv._encode())),
        )
        val paymentMethod = JsonObject(
            mapOf("credit_card" to creditCard),
        )
        return JsonObject(
            mapOf("payment_method" to paymentMethod),
        )
    }
}
