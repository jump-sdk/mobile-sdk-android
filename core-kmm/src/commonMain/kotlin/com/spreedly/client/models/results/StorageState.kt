package com.spreedly.client.models.results

enum class StorageState {
    CACHED, RETAINED, REDACTED, USED, CLOSED;

    companion object {
        fun fromString(s: String): StorageState? = entries.find {
            it.name.lowercase() == s.lowercase()
        }
    }
}
