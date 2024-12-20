package com.spreedly.client.models.results

import kotlinx.datetime.Instant

@Suppress("LongParameterList")
class CreditCardResult(
    override val token: String?,
    override val storageState: String?,
    override val test: Boolean?,
    override val paymentMethodType: String?,
    override val errors: List<SpreedlyError>?,
    override val createdAt: Instant?,
    override val updatedAt: Instant?,
    override val email: String?,
    val lastFourDigits: String?,
    val firstSixDigits: String?,
    val cvv: String?,
    val cardType: String?,
    val number: String?,
    val month: String?,
    val year: String?,
) : PaymentMethodResult(
    token = token,
    storageState = storageState,
    test = test,
    paymentMethodType = paymentMethodType,
    createdAt = createdAt,
    updatedAt = updatedAt,
    email = email,
    errors = errors,
)
