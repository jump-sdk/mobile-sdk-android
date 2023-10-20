package com.spreedly.client.models.results

import kotlinx.datetime.Instant

@Suppress("LongParameterList")
sealed class PaymentMethodResult(
    open val token: String?,
    open val storageState: String?,
    open val test: Boolean?,
    open val paymentMethodType: String?,
    open val createdAt: Instant?,
    open val updatedAt: Instant?,
    open val email: String?,
    open val errors: List<SpreedlyError>?,
)
