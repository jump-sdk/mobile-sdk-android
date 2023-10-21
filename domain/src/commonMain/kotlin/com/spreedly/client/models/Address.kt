package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

data class Address(
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val state: String? = null,
    val zip: String? = null,
    val country: String? = null,
    val phoneNumber: String? = null,
) {

    fun toJson(jsonObject: JsonObject?, prefix: String): JsonObject {
        val jsonEntries: MutableMap<String, JsonElement> =
            jsonObject?.toMutableMap() ?: mutableMapOf()
        return JsonObject(toJson(jsonEntries, prefix))
    }

    fun toJson(
        jsonEntries: MutableMap<String, JsonElement>,
        prefix: String,
    ): MutableMap<String, JsonElement> {
        jsonEntries.putAsJsonElement(prefix + "address1", address1)
        jsonEntries.putAsJsonElement(prefix + "address2", address2)
        jsonEntries.putAsJsonElement(prefix + "city", city)
        jsonEntries.putAsJsonElement(prefix + "state", state)
        jsonEntries.putAsJsonElement(prefix + "zip", zip)
        jsonEntries.putAsJsonElement(prefix + "country", country)
        jsonEntries.putAsJsonElement(prefix + "phone_number", phoneNumber)
        return jsonEntries
    }

    companion object {
        fun fromJson(json: JsonObject, prefix: String): Address? {
            return if (json.containsKey(prefix + "address1")) {
                Address(
                    address1 = json[prefix + "address1"]?.jsonPrimitive?.contentOrNull,
                    address2 = json[prefix + "address2"]?.jsonPrimitive?.contentOrNull,
                    city = json[prefix + "city"]?.jsonPrimitive?.contentOrNull,
                    state = json[prefix + "state"]?.jsonPrimitive?.contentOrNull,
                    zip = json[prefix + "zip"]?.jsonPrimitive?.contentOrNull,
                    country = json[prefix + "country"]?.jsonPrimitive?.contentOrNull,
                    phoneNumber = json[prefix + "phone_number"]?.jsonPrimitive?.contentOrNull,
                )
            } else {
                null
            }
        }
    }
}
