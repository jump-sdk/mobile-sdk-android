package com.spreedly.client.models

import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.client.models.enums.validateNumberLength

private const val MIN_YEAR = 23

val SpreedlySecureOpaqueString.cardBrand: CardBrand
    get() = if (this._encode().length < 16) {
        this.softDetect()
    } else {
        this.detectCardType()
    }

val SpreedlySecureOpaqueString.isValidCreditCard: Boolean
    get() = cardBrand.let { it.isValid && it.validateNumberLength(this._encode()) }

@Suppress("MagicNumber")
val CardBrand?.maxCvcLength: Int
    get() = when (this) {
        CardBrand.unknown, CardBrand.error, null, CardBrand.americanExpress -> 4
        else -> 3
    }

fun SpreedlySecureOpaqueString.isValidCvc(cardBrand: CardBrand?): Boolean = when (cardBrand) {
    CardBrand.unknown, CardBrand.error, null -> this._encode().length in 3..4
    else -> this._encode().length == cardBrand.maxCvcLength
}

fun validatedMonthAndYear(month: Int?, year: Int?): Pair<Int, Int>? {
    @Suppress("ComplexCondition", "MagicNumber")
    return if (month != null && month in 1..12 && year != null && year >= MIN_YEAR) {
        month to year
    } else {
        null
    }
}
