package com.spreedly.client.models.results

import java.util.Date

class CreditCardResult(
    token: String,
    storageState: String?,
    test: Boolean,
    paymentMethodType: String?,
    errors: List<SpreedlyError>?,
    createdAt: Date?,
    updatedAt: Date?,
    email: String?,
    val lastFourDigits: String?,
    val firstSixDigits: String?,
    val cvv: String?,
    val cardType: String?,
    val number: String?,
    val month: String?,
    val year: String?
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