package com.spreedly.composewidgets.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.spreedly.client.models.enums.CardBrand

object CreditCardNumberTransformation {
    fun forBrand(cardBrand: CardBrand, separator: String): VisualTransformation {
        require(separator.length == 1) { "Separator must be a single character" }

        return when (cardBrand) {
            CardBrand.americanExpress -> AmExTransformation(separator)
            CardBrand.dinersClub -> DinersClubTransformation(separator)
            else -> FourDigitChunkTransformation(separator)
        }
    }
}
@Suppress("MagicNumber", "AvoidVarsExceptWithDelegate")
class AmExTransformation(private val separator: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""

        for (i in text.indices) {
            out += text[i]
            if (i == 3 || i == 9 && i != 14) out += separator
        }

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
}

@Suppress("MagicNumber", "AvoidVarsExceptWithDelegate")
class DinersClubTransformation(private val separator: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
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
}

@Suppress("MagicNumber", "AvoidVarsExceptWithDelegate")
class FourDigitChunkTransformation(private val separator: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
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
