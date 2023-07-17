package com.spreedly.client.models.results

import java.util.Date

open class PaymentMethodResult(
    val token: String?,
    val storageState: String?,
    val test: Boolean,
    //maybe enum
    val paymentMethodType: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val email: String?,
    val errors: List<SpreedlyError>?
)