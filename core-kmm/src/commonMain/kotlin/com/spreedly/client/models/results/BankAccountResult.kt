package com.spreedly.client.models.results

import kotlinx.datetime.Instant

data class BankAccountResult(
    override val token: String?,
    override val storageState: String?,
    override val test: Boolean?,
    override val paymentMethodType: String?,
    override val createdAt: Instant?,
    override val updatedAt: Instant?,
    override val email: String?,
    override val errors: List<SpreedlyError>?,
    val bankName: String?,
    val accountType: String?,
    val accountHolderType: String?,
    val routingNumberDisplayDigits: String?,
    val accountNumberDisplayDigits: String?,
    val routingNumber: String?,
    val accountNumber: String?,
    val firstName: String?,
    val lastName: String?,
    val fullName: String?,
) : PaymentMethodResult
