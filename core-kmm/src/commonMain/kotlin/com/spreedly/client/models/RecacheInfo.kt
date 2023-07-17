package com.spreedly.client.models

import org.json.JSONObject

class RecacheInfo(val cvv: SpreedlySecureOpaqueString) {
    fun toJson(): JSONObject {
        val wrapper = JSONObject()
        val paymentMethod = JSONObject()
        val creditCard = JSONObject()
        creditCard.put("verification_value", cvv._encode())
        paymentMethod.put("credit_card", creditCard)
        wrapper.put("payment_method", paymentMethod)
        return wrapper
    }
}