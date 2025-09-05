package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

@Suppress("LongParameterList")
sealed class PaymentMethodInfo(
    open val company: String? = null,
    open val firstName: String? = null,
    open val lastName: String? = null,
    open val fullName: String? = null,
    open val address: Address? = null,
    open val shippingAddress: Address? = null,
    open val retained: Boolean? = null,
    open val retainOnSuccess: Boolean = true,
    open val provisionNetworkToken: Boolean = true,
    open val metadata: JsonObject? = null,
    open val email: String? = null,
) {
    abstract fun toJson(): JsonObject

    fun generateBaseRequestMap(): MutableMap<String, JsonElement> =
        mutableMapOf<String, JsonElement>().apply {
            putAsJsonElement("retained", retained)
            putAsJsonElement("retain_on_success", retainOnSuccess)
            putAsJsonElement("provision_network_token", provisionNetworkToken)
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
            address?.toJson(this, "")
            shippingAddress?.toJson(this, "shipping_")
        }
}
