package com.spreedly.client.models

import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class CreditCardInfoBuilderTest {
    @Test
    fun shouldBuildWhenAllInfoAvailable() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 12
        builder.year = 23
        assertNotNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithMissingYear() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 12
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithInvalidYear() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 12
        builder.year = 20
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithMissingMonth() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.year = 2020
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithInvalidMonth() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 20
        builder.year = 2023
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithMissingFullName() {
        val builder = CreditCardInfoBuilder()
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 12
        builder.year = 2023
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithMissingCardNumber() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cvc = SpreedlySecureOpaqueString("123")
        builder.month = 12
        builder.year = 2023
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithMissingCvc() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.month = 12
        builder.year = 2023
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithInvalidCvc() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("4111111111111111")
        builder.cvc = SpreedlySecureOpaqueString("1")
        builder.month = 12
        builder.year = 2023
        assertNull(builder.build())
    }

    @Test
    fun shouldNotBuildWithInvalidardNumber() {
        val builder = CreditCardInfoBuilder()
        builder.fullName = "John Doe"
        builder.cardNumber = SpreedlySecureOpaqueString("41111")
        builder.cvc = SpreedlySecureOpaqueString("1")
        builder.month = 12
        builder.year = 2023
        assertNull(builder.build())
    }
}
