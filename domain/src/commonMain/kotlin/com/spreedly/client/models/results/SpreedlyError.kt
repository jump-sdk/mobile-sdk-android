package com.spreedly.client.models.results

data class SpreedlyError(
    val attribute: String?,
    val key: String?,
    override val message: String?,
) : Throwable(message)
