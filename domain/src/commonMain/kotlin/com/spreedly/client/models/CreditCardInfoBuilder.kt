package com.spreedly.client.models

class CreditCardInfoBuilder(
    private val postalCodeRequired: Boolean = false,
    private val addressRequired: Boolean = false,
) {
    var postalCode: String? = null
    var streetAddress: String? = null
    var city: String? = null
    var state: String? = null
    var fullName: String? = null
    var cardNumber: SpreedlySecureOpaqueString? = null
    var cvc: SpreedlySecureOpaqueString? = null
    var month: Int? = null
    var year: Int? = null
    var retained: Boolean? = null

    fun build(): CreditCardInfo? {
        @Suppress("SwallowedException")
        return if ((postalCodeRequired && postalCode == null) ||
            (addressRequired && (streetAddress == null || city == null || state == null))) {
            null
        } else {
            try {
                CreditCardInfo(
                    firstName = null,
                    lastName = null,
                    fullName = requireNotNull(fullName),
                    number = requireNotNull(cardNumber),
                    verificationValue = requireNotNull(cvc),
                    month = requireNotNull(month),
                    year = requireNotNull(year),
                    address = if (postalCode != null || streetAddress != null || city != null || state != null) {
                        Address(
                            address1 = streetAddress,
                            city = city,
                            state = state,
                            zip = postalCode,
                        )
                    } else {
                        null
                    },
                    retained = retained,
                ).takeIf {
                    it.prevalidate()
                }
            } catch (e: IllegalArgumentException) {
                null
            }
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
        if (postalCode != other.postalCode) return false
        if (streetAddress != other.streetAddress) return false
        if (city != other.city) return false
        if (state != other.state) return false
        return year == other.year
    }

    override fun hashCode(): Int {
        var result = fullName?.hashCode() ?: 0
        result = 31 * result + (cardNumber?.hashCode() ?: 0)
        result = 31 * result + (cvc?.hashCode() ?: 0)
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        result = 31 * result + (postalCode?.hashCode() ?: 0)
        result = 31 * result + (streetAddress?.hashCode() ?: 0)
        result = 31 * result + (city?.hashCode() ?: 0)
        result = 31 * result + (state?.hashCode() ?: 0)
        return result
    }
}
