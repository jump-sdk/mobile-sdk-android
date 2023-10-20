package com.spreedly.client.models

data class PartialCreditCardInfo(
    val fullName: String,
    val month: Int = 0,
    val year: Int = 0,
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
}
