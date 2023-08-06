package com.spreedly.client.models

import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.client.models.enums.validateNumberLength

data class SecureCreditCardNumber(
    val number: SpreedlySecureOpaqueString,
    val isValid: Boolean,
    val brand: CardBrand,
) {
    constructor(number: SpreedlySecureOpaqueString) : this(
        number = number,
        isValid = number.cardBrand.let { it.isValid && it.validateNumberLength(number._encode()) },
        brand = number.cardBrand,
    )
}

val SpreedlySecureOpaqueString.cardBrand: CardBrand
    get() = if (this._encode().length < 16) {
        this.softDetect()
    } else {
        this.detectCardType()
    }
