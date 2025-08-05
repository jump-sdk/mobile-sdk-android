package com.spreedly.client.models

data class PartialCreditCardInfo(
    val fullName: String,
    val month: Int,
    val year: Int,
    val postalCode: String,
    var streetAddress: String,
    var city: String,
    var state: String,
)

class PartialCreditCardInfoBuilder(
    private val addressRequired: Boolean = false,
) {
    var postalCode: String? = null
    var streetAddress: String? = null
    var city: String? = null
    var state: String? = null
    var fullName: String? = null
    var month: Int? = null
    var year: Int? = null

    fun build(): PartialCreditCardInfo? {
        val expiry = validatedMonthAndYear(month = month, year = year)
        val postalCode = postalCode
        val fullName = fullName
        val isAddressValid =
            !addressRequired || (streetAddress != null && city != null && state != null)
        return if (postalCode != null && fullName != null && expiry != null && isAddressValid) {
            PartialCreditCardInfo(
                fullName = fullName,
                month = expiry.first,
                year = expiry.second,
                postalCode = postalCode,
                streetAddress = streetAddress.orEmpty(),
                city = city.orEmpty(),
                state = state.orEmpty(),
            )
        } else {
            null
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PartialCreditCardInfoBuilder

        if (postalCode != other.postalCode) return false
        if (fullName != other.fullName) return false
        if (month != other.month) return false
        return year == other.year
    }

    override fun hashCode(): Int {
        var result = postalCode?.hashCode() ?: 0
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (month ?: 0)
        result = 31 * result + (year ?: 0)
        return result
    }
}
