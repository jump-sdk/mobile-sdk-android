package com.spreedly.client.models.results

import java.util.Date

/// Bank Account Result
class BankAccountResult(
    token: String?,
    storageState: String?,
    test: Boolean,
    paymentMethodType: String?,
    createdAt: Date?,
    updatedAt: Date?,
    email: String?,
    errors: List<SpreedlyError>?,
    val bankName: String?,
    val accountType: String?,
    val accountHolderType: String?,
    val routingNumberDisplayDigits: String?,
    val accountNumberDisplayDigits: String?,
    val routingNumber: String?,
    val accountNumber: String?,
    val firstName: String?,
    val lastName: String?,
    val fullName: String?
) : PaymentMethodResult(
    token,
    storageState,
    test,
    paymentMethodType,
    createdAt,
    updatedAt,
    email,
    errors
)