package com.spreedly.client.models

import com.spreedly.client.models.enums.CardBrand

class SpreedlySecureOpaqueString(private var data: String) {
    val length: Int get() = data.length

    val lastFour = data.takeLast(4)

    override fun toString(): String = "SpreedlySecureOpaqueString(length=$length)"

    fun clear() {
        data = ""
    }

    fun append(string: String) {
        data += string
    }

    fun _encode(): String {
        data = data.replace(" ", "")
        return data
    }

    fun detectCardType(): CardBrand {
        val data = data.replace(" ", "")
        if (!checkIsValid(data) || data.length > 19) {
            return CardBrand.error
        }
        if (Regex("^4[0-9]{12}([0-9]{3})?([0-9]{3})?$").matches(data)) {
            return CardBrand.visa
        } else if (data.length == 16 && inRanges(CardBrand.mastercard.range, data, 6)) {
            return CardBrand.mastercard
        } else if (data.length == 16 && inRanges(CardBrand.elo.range, data, 6)) {
            return CardBrand.elo
        } else if (data.length == 16 && inRanges(CardBrand.alelo.range, data, 6)) {
            return CardBrand.alelo
        } else if (
            Regex("^(6011|65[0-9]{2}|64[4-9][0-9])[0-9]{12,15}|(62[0-9]{14,17})$")
                .matches(data) && !Regex("^627416[0-9]{10}$").matches(data)
        ) {
            return CardBrand.discover
        } else if (Regex("^3[47][0-9]{13}$").matches(data)) {
            return CardBrand.americanExpress
        } else if (data.length == 16 && binMatch(CardBrand.naranja.bins, data)) {
            return CardBrand.naranja
        } else if (Regex("^3(0[0-5]|[68][0-9])[0-9]{11}$").matches(data)) {
            return CardBrand.dinersClub
        } else if (Regex("^35(28|29|[3-8][0-9])[0-9]{12}$").matches(data)) {
            return CardBrand.jcb
        } else if (Regex("^5019[0-9]{12}$").matches(data)) {
            return CardBrand.dankort
        } else if (data.length >= 12 && (
                inRanges(CardBrand.maestro.range, data, 6) || binMatch(
                    CardBrand.maestro.bins,
                    data,
                )
                )
        ) {
            return CardBrand.maestro
        } else if (
            Regex("^(606071|603389|606070|606069|606068|600818)[0-9]{10}$").matches(data)
        ) {
            return CardBrand.sodexo
        } else if (Regex("^(627416|637036)[0-9]{10}$").matches(data)) {
            return CardBrand.vr
        } else if (data.length == 16 && inRanges(CardBrand.cabal.range, data, 8)) {
            return CardBrand.cabal
        } else if (data.length == 16 && inRanges(CardBrand.carnet.range, data, 6) || binMatch(
                CardBrand.carnet.bins,
                data,
            )
        ) {
            return CardBrand.carnet
        }
        return CardBrand.unknown
    }

    fun softDetect(): CardBrand {
        val data = data.replace(" ", "")
        if (length > 19) {
            return CardBrand.unknown
        }
        if (data.startsWith("4")) {
            return CardBrand.visa
        } else if (inRanges(CardBrand.mastercard.range, data, 6)) {
            return CardBrand.mastercard
        } else if (inRanges(CardBrand.elo.range, data, 6)) {
            return CardBrand.elo
        } else if (inRanges(CardBrand.alelo.range, data, 6)) {
            return CardBrand.alelo
        } else if (
            Regex("^(6011|65[0-9]{2}|64[4-9][0-9])[0-9]{12,15}|(62[0-9]{14,17})$")
                .matches(data) && !Regex("^627416[0-9]{10}$").matches(data)
        ) {
            return CardBrand.discover
        } else if (Regex("^3[47][0-9]*").matches(data)) {
            return CardBrand.americanExpress
        } else if (binMatch(CardBrand.naranja.bins, data)) {
            return CardBrand.naranja
        } else if (Regex("^3(0[0-5]|[68][0-9])[0-9]*").matches(data)) {
            return CardBrand.dinersClub
        } else if (Regex("^35(28|29|[3-8][0-9]*)").matches(data)) {
            return CardBrand.jcb
        } else if (Regex("^5019[0-9]*").matches(data)) {
            return CardBrand.dankort
        } else if (inRanges(CardBrand.maestro.range, data, 6) || binMatch(
                CardBrand.maestro.bins,
                data,
            )
        ) {
            return CardBrand.maestro
        } else if (Regex("^(606071|603389|606070|606069|606068|600818)[0-9]*").matches(data)) {
            return CardBrand.sodexo
        } else if (Regex("^(627416|637036)[0-9]{10}$").matches(data)) {
            return CardBrand.vr
        } else if (inRanges(CardBrand.cabal.range, data, 8)) {
            return CardBrand.cabal
        } else if (inRanges(CardBrand.carnet.range, data, 6) || binMatch(
                CardBrand.carnet.bins,
                data,
            )
        ) {
            return CardBrand.carnet
        }
        return CardBrand.unknown
    }

    private fun checkIsValid(numbers: String): Boolean {
        try {
            numbers.toDouble()
        } catch (e: Exception) {
            return false
        }
        val nDigits = numbers.length
        var nSum = 0
        var isSecond = false
        for (i in nDigits - 1 downTo 0) {
            var d: Int = numbers[i].code - '0'.code
            if (isSecond == true) d = d * 2
            nSum += d / 10
            nSum += d % 10
            isSecond = !isSecond
        }
        return nSum % 10 == 0
    }

    val isNumber: Boolean
        get() {
            data = data.replace(" ".toRegex(), "")
            return try {
                data.toDouble()
                true
            } catch (e: Exception) {
                false
            }
        }

    private fun inRanges(ranges: List<IntRange>?, input: String, length: Int): Boolean {
        if (length > input.length) { return false }
        if (ranges == null) { return true }
        val number = input.substring(0, length).toInt()
        ranges.forEach {
            if (it.contains(number)) {
                return true
            }
        }
        return false
    }

    private fun binMatch(bins: List<String>?, number: String): Boolean {
        if (bins == null) { return true }
        bins.forEach {
            if (number.startsWith(it)) {
                return true
            }
        }
        return false
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SpreedlySecureOpaqueString

        if (data != other.data) return false

        return true
    }

    override fun hashCode(): Int = data.hashCode()
}
