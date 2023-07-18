package com.spreedly.client.models.results

import kotlinx.datetime.Instant

interface PaymentMethodResult {
    val token: String?
    val storageState: String?
    val test: Boolean
    val paymentMethodType: String?
    val createdAt: Instant?
    val updatedAt: Instant?
    val email: String?
    val errors: List<SpreedlyError>?
}
