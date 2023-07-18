package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

abstract class PaymentMethodInfo(
    val company: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val fullName: String? = null,
    val address: Address? = null,
    val shippingAddress: Address? = null,
    val retained: Boolean? = null,
    val metadata: JsonObject? = null,
    val email: String? = null,
) {
    abstract fun toJson(): JsonObject

    fun addCommonJsonFields(
        paymentMethodEntries: MutableMap<String, JsonElement>,
        subTypeEntries: MutableMap<String, JsonElement>,
    ) {
        if (fullName != null) {
            subTypeEntries.putAsJsonElement("full_name", fullName)
        } else {
            subTypeEntries.putAsJsonElement("first_name", firstName)
            subTypeEntries.putAsJsonElement("last_name", lastName)
        }
        subTypeEntries.putAsJsonElement("company", company)
        address?.let { it.toJson(subTypeEntries, "") }
        shippingAddress?.let { it.toJson(subTypeEntries, "shipping_") }
        paymentMethodEntries.putAsJsonElement("retained", retained)
        metadata?.let {
            paymentMethodEntries.put("metadata", it)
        }
        email?.let {
            paymentMethodEntries.putAsJsonElement("email", it)
        }
    }
}
