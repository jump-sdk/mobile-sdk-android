package com.spreedly.client.models.results

import kotlinx.datetime.Instant

data class TransactionResult<T>(
    val token: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val succeeded: Boolean,
    // maybe enum
    val transactionType: String?,
    val retained: Boolean,
    // maybe enum
    val state: String?,
    // localization?
    val messageKey: String?,
    val message: String?,
    val errors: List<SpreedlyError>?,
    val result: T?
)
