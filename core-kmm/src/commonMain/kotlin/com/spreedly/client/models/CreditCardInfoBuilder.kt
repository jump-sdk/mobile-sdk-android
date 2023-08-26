package com.spreedly.client.models

class CreditCardInfoBuilder {
    var postalCode: String? = null
    var fullName: String? = null
    var cardNumber: SpreedlySecureOpaqueString? = null
    var cvc: SpreedlySecureOpaqueString? = null
    var month: Int? = null
    var year: Int? = null
    var retained: Boolean? = null

    fun build(): CreditCardInfo? {
        @Suppress("SwallowedException")
        return try {
            CreditCardInfo(
                firstName = null,
                lastName = null,
                fullName = requireNotNull(fullName),
                number = requireNotNull(cardNumber),
                verificationValue = requireNotNull(cvc),
                month = requireNotNull(month),
                year = requireNotNull(year),
                address = postalCode?.let { Address(zip = it) },
                retained = retained,
            ).takeIf {
                it.prevalidate()
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreditCardInfoBuilder

        if (fullName != other.fullName) return false
        if (cardNumber != other.cardNumber) return false
        if (cvc != other.cvc) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fullName?.hashCode() ?: 0
        result = 31 * result + (cardNumber?.hashCode() ?: 0)
        result = 31 * result + (cvc?.hashCode() ?: 0)
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        return result
    }
}
