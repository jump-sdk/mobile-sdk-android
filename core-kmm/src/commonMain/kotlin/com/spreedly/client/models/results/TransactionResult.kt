package com.spreedly.client.models.results

import kotlinx.datetime.Instant

data class TransactionResult<T>(
    @Deprecated("You probably want result.token instead")
    val transactionToken: String?,
    val createdAt: Instant?,
    val updatedAt: Instant?,
    val succeeded: Boolean,
    // maybe enum
    val transactionType: String?,
    val retained: Boolean,
    val storageState: StorageState?,
    @Deprecated("Use storageState instead")
    val state: String?,
    // localization?
    val messageKey: String?,
    val message: String?,
    val errors: List<SpreedlyError>?,
    val result: T?,
)
