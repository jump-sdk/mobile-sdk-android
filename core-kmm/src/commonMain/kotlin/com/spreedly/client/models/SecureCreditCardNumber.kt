package com.spreedly.client.models

import com.spreedly.client.models.enums.CardBrand

data class SecureCreditCardNumber(
    val number: SpreedlySecureOpaqueString,
    val isValid: Boolean,
    val brand: CardBrand,
) {
    constructor(number: SpreedlySecureOpaqueString) : this(
        number = number,
        isValid = number.isValidCreditCard,
        brand = number.cardBrand,
    )
}
