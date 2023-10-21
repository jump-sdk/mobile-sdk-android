package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

fun MutableMap<String, JsonElement>.putAsJsonElement(key: String, value: String?) {
    value?.also {
        this.put(key, JsonPrimitive(it))
    } ?: run {
        this.remove(key)
    }
}

internal fun MutableMap<String, JsonElement>.putAsJsonElement(key: String, value: Boolean?) {
    value?.also {
        this.put(key, JsonPrimitive(it))
    } ?: run {
        this.remove(key)
    }
}

internal fun MutableMap<String, JsonElement>.putAsJsonElement(key: String, value: Int?) {
    value?.also {
        this.put(key, JsonPrimitive(it))
    } ?: run {
        this.remove(key)
    }
}
