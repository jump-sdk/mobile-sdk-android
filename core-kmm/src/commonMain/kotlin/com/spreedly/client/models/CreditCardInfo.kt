package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class CreditCardInfo(
    firstName: String,
    lastName: String,
    val number: SpreedlySecureOpaqueString,
    val verificationValue: SpreedlySecureOpaqueString,
    val month: Int = 0,
    val year: Int = 0,
    retained: Boolean = false,
) : PaymentMethodInfo(
    firstName = firstName,
    lastName = lastName,
    retained = retained,
) {
    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val creditCard = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, creditCard)
        println(creditCard)
        creditCard.putAsJsonElement("verification_value", verificationValue._encode())
        println(creditCard)
        creditCard.put("verification_value", JsonPrimitive("wrong value"))
        println(creditCard)
        creditCard.putAsJsonElement("number", number._encode())
        creditCard.putAsJsonElement("month", month)
        creditCard.putAsJsonElement("year", year)
        println(creditCard)
        paymentMethod.put("credit_card", JsonObject(creditCard))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
