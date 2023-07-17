package com.spreedly.client.models.results

import java.io.Serializable
import java.util.Date

class TransactionResult<T>(
    val token: String?,
    val createdAt: Date?,
    val updatedAt: Date?,
    val succeeded: Boolean,
    // maybe enum
    val transactionType: String?,
    val retained: Boolean,
    // maybe enum
    val state: String?,
    // localization?
    val messageKey: String?,
    message: String?,
    errors: List<SpreedlyError>?,
    result: T?
) : Serializable {
    @kotlin.jvm.JvmField
    val message: String?
    @kotlin.jvm.JvmField
    val errors: List<SpreedlyError>?
    @kotlin.jvm.JvmField
    val result: T?

    init {
        this.message =
            if ((message == null || message.isEmpty()) && errors != null && !errors.isEmpty()) errors[0].message else message
        this.result = result
        this.errors = errors
    }
}