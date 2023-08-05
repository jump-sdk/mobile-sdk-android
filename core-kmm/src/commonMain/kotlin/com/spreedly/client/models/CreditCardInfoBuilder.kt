package com.spreedly.client.models

data class CreditCardInfoBuilder(
    val fullName: String? = null,
    val cardNumber: SpreedlySecureOpaqueString? = null,
    val cvc: SpreedlySecureOpaqueString? = null,
    val month: Int? = null,
    val year: Int? = null,
) {
    fun build(): CreditCardInfo? {
        return try {
            CreditCardInfo(
                firstName = null,
                lastName = null,
                fullName = requireNotNull(fullName),
                number = requireNotNull(cardNumber),
                verificationValue = requireNotNull(cvc),
                month = requireNotNull(month),
                year = requireNotNull(year),
            )
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
