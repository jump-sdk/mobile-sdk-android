package com.spreedly.composewidgets

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class ExpirationDateTransformation(private val separator: String) : VisualTransformation {
    private val separatorSize = separator.length
    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""

        for (i in text.indices) {
            out += text[i]
            if (i == 1) out += separator
        }

        val offsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                if (offset <= 1) offset else offset + separatorSize

            override fun transformedToOriginal(offset: Int): Int =
                if (offset <= 2) offset else offset - separatorSize
        }

        return TransformedText(
            AnnotatedString(out),
            offsetTranslator,
        )
    }
}
