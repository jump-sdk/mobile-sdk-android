package com.spreedly.composewidgets

import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.client.models.enums.maxNumberLength
import com.spreedly.client.models.enums.validateNumberLength

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureCreditCardField(
    onValueChange: (SecureCreditCardNumber) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier = Modifier,
    separator: String = " ",
    label: @Composable (() -> Unit)?,
) {
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }
    var transformation by remember(brand) {
        mutableStateOf(CreditCardNumberTransformation(brand, separator))
    }

    SecureTextField(
        modifier = modifier,
        autofill = AutofillType.CreditCardNumber,
        onValueChange = { cardNumber ->
            brand = cardNumber.cardBrand
            onValueChange(
                SecureCreditCardNumber(
                    number = cardNumber,
                    isValid = brand.isValid && brand.validateNumberLength(cardNumber._encode()),
                    brand = brand,
                ),
            )
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        maxValueLength = brand.maxNumberLength,
        visualTransformation = transformation,
    )
}

data class SecureCreditCardNumber(
    val number: SpreedlySecureOpaqueString,
    val isValid: Boolean,
    val brand: CardBrand,
)

private val SpreedlySecureOpaqueString.cardBrand: CardBrand
    get() = if (this._encode().length < 16) {
        this.softDetect()
    } else {
        this.detectCardType()
    }
