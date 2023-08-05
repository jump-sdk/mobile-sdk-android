package com.spreedly.composewidgets

import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureVerificationNumberField(
    onValueChange: (SpreedlySecureOpaqueString, Boolean) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    cardBrand: CardBrand? = null,
    modifier: Modifier,
    label: @Composable (() -> Unit)?,
) = SecureTextField(
    modifier = modifier,
    autofill = AutofillType.CreditCardSecurityCode,
    onValueChange = {
        val isValid = when (cardBrand) {
            null -> it._encode().length == 4 || it._encode().length == 3
            CardBrand.americanExpress -> it._encode().length == 4
            else -> it._encode().length == 3
        }
        onValueChange(it, isValid)
    },
    label = label,
    textStyle = textStyle,
    shape = shape,
    colors = colors,
    maxValueLength = when (cardBrand) {
        null, CardBrand.americanExpress -> 4
        else -> 3
    },
    visualTransformation = VisualTransformation.None,
)
