package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

abstract class PaymentMethodInfo(
    open val company: String? = null,
    open val firstName: String? = null,
    open val lastName: String? = null,
    open val fullName: String? = null,
    open val address: Address? = null,
    open val shippingAddress: Address? = null,
    open val retained: Boolean? = null,
    open val metadata: JsonObject? = null,
    open val email: String? = null,
) {
    abstract fun toJson(): JsonObject

    fun generateBaseRequestMap(): MutableMap<String, JsonElement> =
        mutableMapOf<String, JsonElement>().apply {
            putAsJsonElement("retained", retained)
            metadata?.let {
                put("metadata", it)
            }
            email?.let {
                putAsJsonElement("email", it)
            }
        }

    fun generatePersonInfoMap(): MutableMap<String, JsonElement> =
        mutableMapOf<String, JsonElement>().apply {
            if (fullName != null) {
                putAsJsonElement("full_name", fullName)
            } else {
                putAsJsonElement("first_name", firstName)
                putAsJsonElement("last_name", lastName)
            }
            putAsJsonElement("company", company)
            address?.let { it.toJson(this, "") }
            shippingAddress?.let { it.toJson(this, "shipping_") }
        }
}
