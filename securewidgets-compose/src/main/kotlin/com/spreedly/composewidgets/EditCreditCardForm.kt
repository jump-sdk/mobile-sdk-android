package com.spreedly.composewidgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spreedly.client.models.PartialCreditCardInfo
import com.spreedly.client.models.PartialCreditCardInfoBuilder

/**
 * Composable function that creates a secure credit card input form for collecting credit card information.
 *
 * @param initialFullName The initial value for the full name input field.
 * @param initialPostalCode The initial value for the postal code input field.
 * @param initialExpiryMonth The initial value for the expiry month input field.
 * @param initialExpiryYear The initial value for the expiry year input field.
 * @param fieldSpacing The vertical spacing between the input fields.
 * @param modifier The modifier for the entire credit card form.
 * @param fieldModifier The modifier for individual input fields.
 * @param showPostalCodeField Whether or not to show the postal code input field.
 * @param textStyle The text style to be applied to the input fields.
 * @param colors The colors customization for the input fields.
 * @param shape The shape customization for the input fields.
 * @param labelFactory The composable function used to render the labels for input fields.
 *                      Takes a hint string as a parameter.
 * @param textFieldPadding The padding to be applied to the input fields.
 * @param onCreditCardInfo The callback function triggered when valid credit card information is provided.
 *                              Will be null if internal validation fails.
 */
@OptIn(ExperimentalMaterialApi::class)
@Suppress("LongMethod", "LongParameterList", "ModifierParameterPosition")
@Composable
fun EditCreditCardForm(
    initialFullName: String,
    initialPostalCode: String,
    initialExpiryMonth: String,
    initialExpiryYear: String,
    fieldSpacing: Dp,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    showPostalCodeField: Boolean = true,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    labelFactory: @Composable (String) -> Unit = { Text(it) },
    textFieldPadding: PaddingValues = TextFieldDefaults.outlinedTextFieldPadding(),
    onCreditCardInfo: (PartialCreditCardInfo?) -> Unit,
) {
    val creditCardInfoBuilder = rememberSaveable(
        saver = PartialCreditCardInfoBuilderSaver,
    ) {
        PartialCreditCardInfoBuilder().apply {
            fullName = initialFullName
            month = initialExpiryMonth.toIntOrNull()
            year = initialExpiryYear.toIntOrNull()
            postalCode = initialPostalCode
        }
    }

    val expirationChanged: (ValidatedExpirationDate?) -> Unit = { validatedDate ->
        validatedDate
            ?.getValidatedMonthAndYear()
            ?.let { (month, year) ->
                creditCardInfoBuilder.month = month
                creditCardInfoBuilder.year = year
            }
            ?: run {
                creditCardInfoBuilder.month = null
                creditCardInfoBuilder.year = null
            }
        onCreditCardInfo(creditCardInfoBuilder.build())
    }

    Column(modifier, verticalArrangement = Arrangement.spacedBy(fieldSpacing)) {
        NameField(
            modifier = fieldModifier,
            onValueChange = {
                creditCardInfoBuilder.fullName = it.ifBlank { null }
                onCreditCardInfo(creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.card_name_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            textFieldPadding = textFieldPadding,
            initialValue = initialFullName,
        )
        ExpirationField(
            modifier = fieldModifier,
            onValueChange = expirationChanged,
            label = { labelFactory(stringResource(id = R.string.expiration_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            initialValue = ValidatedExpirationDate(
                month = initialExpiryMonth.toIntOrNull(),
                year = initialExpiryYear.toIntOrNull(),
            ),
            textFieldPadding = textFieldPadding,
        )

        if (showPostalCodeField) {
            PostalCodeField(
                modifier = fieldModifier,
                onValueChange = {
                    creditCardInfoBuilder.postalCode = it.ifBlank { null }
                    onCreditCardInfo(creditCardInfoBuilder.build())
                },
                label = { labelFactory(stringResource(id = R.string.postal_code_hint)) },
                textStyle = textStyle,
                colors = colors,
                shape = shape,
                textFieldPadding = textFieldPadding,
                initialValue = initialPostalCode,
            )
        }
    }
}

@Preview
@Composable
private fun SecureCreditCardFormPreview() {
    Surface {
        SecureCreditCardForm(
            fieldSpacing = 16.dp,
            onValidCreditCardInfo = { _, _ -> },
        )
    }
}
