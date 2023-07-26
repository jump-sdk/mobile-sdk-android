package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class CreditCardInfo(
    val number: SpreedlySecureOpaqueString,
    val verificationValue: SpreedlySecureOpaqueString,
    val month: Int = 0,
    val year: Int = 0,
    private val allowBlankName: Boolean? = null,
    private val allowExpiredDate: Boolean? = null,
    private val allowBlankDate: Boolean? = null,
    private val eligibleForCardUpdate: Boolean? = null,
) : PaymentMethodInfo() {
    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val creditCard = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, creditCard)
        creditCard.putAsJsonElement("verification_value", verificationValue._encode())
        creditCard.putAsJsonElement("number", number._encode())
        creditCard.putAsJsonElement("month", month)
        creditCard.putAsJsonElement("year", year)
        paymentMethod.putAsJsonElement("allow_blank_name", allowBlankName)
        paymentMethod.putAsJsonElement("allow_expired_date", allowExpiredDate)
        paymentMethod.putAsJsonElement("allow_blank_date", allowBlankDate)
        paymentMethod.putAsJsonElement("eligible_for_card_updater", eligibleForCardUpdate)
        paymentMethod.put("credit_card", JsonObject(creditCard))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
