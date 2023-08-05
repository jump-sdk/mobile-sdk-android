package com.spreedly.composewidgets

import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureCreditCardField(
    onValueChange: (SecureCreditCardValue) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier = Modifier,
    label: @Composable (() -> Unit)?,
) = SecureTextField(
    modifier = modifier,
    autofill = AutofillType.CreditCardNumber,
    onValueChange = { cardNumber ->
        val brand = cardNumber.cardBrand
        onValueChange(
            SecureCreditCardValue(
                number = cardNumber,
                isValid = cardNumber._encode().length >= 13 && brand.isValid,
                brand = brand,
            ),
        )
    },
    label = label,
    textStyle = textStyle,
    shape = shape,
    colors = colors,
)

data class SecureCreditCardValue(
    val number: SpreedlySecureOpaqueString,
    val isValid: Boolean,
    val brand: CardBrand?,
)

private val SpreedlySecureOpaqueString.cardBrand: CardBrand
    get() = if (this._encode().length < 16) {
        this.softDetect()
    } else {
        this.detectCardType()
    }
