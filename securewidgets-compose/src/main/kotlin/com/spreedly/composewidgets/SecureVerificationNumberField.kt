package com.spreedly.composewidgets

import androidx.compose.foundation.layout.PaddingValues
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
import com.spreedly.client.models.isValidCvc
import com.spreedly.client.models.maxCvcLength

/**
 * Composable function representing a secure input field for entering verification numbers (CVV/CVC).
 *
 * @param onValueChange The callback triggered when the value of the verification number changes.
 *                      It receives the entered verification number as well as a flag indicating its validity.
 * @param textStyle The text style to be applied to the input text.
 * @param shape The shape customization for the verification number input field.
 * @param colors The colors customization for the text field.
 * @param textFieldPadding The padding to be applied to the input field.
 * @param modifier The modifier for the verification number input field.
 * @param cardBrand The brand of the credit card. If unknown or null, will validate with either 3 or 4 digits.
 *                  If AmEx, will validate with 4 digits. Otherwise, will validate with 3 digits.
 * @param label The composable function used to display the label for the input field.
 *              It provides a label as a content parameter.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureVerificationNumberField(
    onValueChange: (SpreedlySecureOpaqueString, Boolean) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    textFieldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    cardBrand: CardBrand,
    label: @Composable () -> Unit,
) = SecureTextField(
    modifier = modifier,
    autofill = AutofillType.CreditCardSecurityCode,
    onValueChange = {
        onValueChange(it, it.isValidCvc(cardBrand))
    },
    label = label,
    textStyle = textStyle,
    shape = shape,
    colors = colors,
    maxValueLength = cardBrand.maxCvcLength,
    visualTransformation = VisualTransformation.None,
    contentPadding = textFieldPadding,
)
