package com.spreedly.client.models

data class PartialCreditCardInfo(
    val fullName: String,
    val month: Int,
    val year: Int,
    val postalCode: String,
)

class PartialCreditCardInfoBuilder {
    var postalCode: String? = null
    var fullName: String? = null
    var month: Int? = null
    var year: Int? = null

    fun build(): PartialCreditCardInfo? {
        val expiry = validatedMonthAndYear(month = month, year = year)
        val postalCode = postalCode
        val fullName = fullName
        return if (postalCode != null && fullName != null && expiry != null) {
            PartialCreditCardInfo(
                fullName = fullName,
                month = expiry.first,
                year = expiry.second,
                postalCode = postalCode,
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
