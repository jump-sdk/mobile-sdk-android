package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

internal fun MutableMap<String, JsonElement>.putAsJsonElement(
    key: String,
    value: String?,
): MutableMap<String, JsonElement> {
    value?.let {
        println("$key, $value")
        this.put(key, JsonPrimitive(it))
        println(this.toString())
    } ?: run {
        this.remove(key)
    }
    return this
}

internal fun MutableMap<String, JsonElement>.putAsJsonElement(key: String, value: Boolean?) {
    value?.let {
        this.put(key, JsonPrimitive(it))
    } ?: run {
        this.remove(key)
    }
}

internal fun MutableMap<String, JsonElement>.putAsJsonElement(key: String, value: Int?) {
    value?.let {
        this.put(key, JsonPrimitive(it))
    } ?: run {
        this.remove(key)
    }
}
