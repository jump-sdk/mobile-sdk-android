package com.spreedly.client.models

import com.spreedly.client.models.results.PaymentMethodResult
import com.spreedly.client.models.results.SpreedlyError
import com.spreedly.client.models.results.TransactionResult
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class TransactionResultEdgeCaseTests {
    private var errors: MutableList<SpreedlyError> = mutableListOf(SpreedlyError("attribute", "key", "error message"))

    @BeforeTest
    fun initialize() {
        errors.add(SpreedlyError("attribute", "key", "error message"))
    }

    @Test
    fun messageHasValue() {
        val transaction: TransactionResult<PaymentMethodResult> =
            TransactionResult(
                token = null,
                createdAt = null,
                updatedAt = null,
                succeeded = false,
                transactionType = null,
                retained = false,
                state = null,
                messageKey = null,
                message = "success message",
                errors = errors,
                result = null,
            )
        assertEquals("success message", transaction.message)
    }

    @Test
    fun errorsIsEmpty() {
        val transaction: TransactionResult<PaymentMethodResult> =
            TransactionResult(
                token = null,
                createdAt = null,
                updatedAt = null,
                succeeded = false,
                transactionType = null,
                retained = false,
                state = null,
                messageKey = null,
                message = null,
                errors = listOf(),
                result = null,
            )
        assertNull(transaction.message)
    }

    @Test
    fun errorsIsNull() {
        val transaction: TransactionResult<PaymentMethodResult> =
            TransactionResult(
                token = null,
                createdAt = null,
                updatedAt = null,
                succeeded = false,
                transactionType = null,
                retained = false,
                state = null,
                messageKey = null,
                message = null,
                errors = null,
                result = null,
            )
        assertNull(transaction.message)
    }
}
