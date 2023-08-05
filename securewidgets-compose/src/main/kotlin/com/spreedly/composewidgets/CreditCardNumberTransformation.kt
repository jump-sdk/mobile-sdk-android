package com.spreedly.composewidgets

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.spreedly.client.models.enums.CardBrand

// https://dev.to/benyam7/formatting-credit-card-number-input-in-jetpack-compose-android-2nal

class CreditCardNumberTransformation(
    private val cardBrand: CardBrand,
    private val separator: String,
) : VisualTransformation {
    init {
        require(separator.length == 1) { "Separator must be a single character" }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        return when (cardBrand) {
            CardBrand.americanExpress -> formatAmex(text)
            CardBrand.dinersClub -> formatDinersClub(text)
            else -> formatOtherCardNumbers(text)
        }
    }

    private fun formatAmex(text: AnnotatedString): TransformedText {
        var out = ""

        for (i in text.indices) {
            out += text[i]
            if (i == 3 || i == 9 && i != 14) out += separator
        }

        /**
         * The offset translator should ignore the hyphen characters, so conversion from
         *  original offset to transformed text works like
         *  - The 4th char of the original text is 5th char in the transformed text. (i.e original[4th] == transformed[5th]])
         *  - The 11th char of the original text is 13th char in the transformed text. (i.e original[11th] == transformed[13th])
         *  Similarly, the reverse conversion works like
         *  - The 5th char of the transformed text is 4th char in the original text. (i.e  transformed[5th] == original[4th] )
         *  - The 13th char of the transformed text is 11th char in the original text. (i.e transformed[13th] == original[11th])
         */
        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in 0..3 -> offset
                    in 4..9 -> offset + 1
                    in 10..15 -> offset + 2
                    else -> 17
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in 0..4 -> offset
                    in 5..11 -> offset - 1
                    in 12..17 -> offset - 2
                    else -> 15
                }
            }
        }
        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }

    private fun formatDinersClub(text: AnnotatedString): TransformedText {
        var out = ""

        for (i in text.indices) {
            out += text[i]
            if (i == 3 || i == 9) out += separator
        }

//    xxxx-xxxxxx-xxxx
        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in 0..3 -> offset
                    in 4..9 -> offset + 1
                    else -> offset + 2
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in 0..4 -> offset
                    in 5..11 -> offset - 1
                    else -> offset - 2
                }
            }
        }
        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }

    private fun formatOtherCardNumbers(text: AnnotatedString): TransformedText {
        var out = ""

        for (i in text.indices) {
            out += text[i]
            if (i % 4 == 3) out += separator
        }
        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return when (offset) {
                    in 0..3 -> offset
                    in 4..7 -> offset + 1
                    in 8..11 -> offset + 2
                    in 12..16 -> offset + 3
                    else -> offset + 4
                }
            }

            override fun transformedToOriginal(offset: Int): Int {
                return when (offset) {
                    in 0..4 -> offset
                    in 5..9 -> offset - 1
                    in 10..14 -> offset - 2
                    in 15..19 -> offset - 3
                    else -> offset - 4
                }
            }
        }

        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }
}
