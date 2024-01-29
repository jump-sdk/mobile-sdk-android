package com.spreedly.composewidgets

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.spreedly.client.models.validatedMonthAndYear
import com.spreedly.composewidgets.utils.ExpirationDateTransformation
import com.spreedly.composewidgets.utils.PaddableOutlinedTextField
import java.util.Locale

/**
 * Composable function representing an input field for entering expiration dates of credit cards.
 *
 * @param onValueChange The callback triggered when the value of the expiration date changes.
 *                      It receives the entered expiration date information. The value is non-null,
 *                      call getValidatedMonthAndYear() to get the month and year integer pair,
 *                      which will itself be null if the month or year are not valid.
 * @param textStyle The text style to be applied to the input text.
 * @param shape The shape customization for the expiration date input field.
 * @param colors The colors customization for the text field.
 * @param textFieldPadding The padding to be applied to the input field.
 * @param modifier The modifier for the expiration date input field.
 * @param initialValue The initial value to populate the expiration date input field. Used to
 *                      update from OCR.
 * @param separator The separator string used to visually separate the month and year values.
 * @param label The composable function used to display the label for the input field.
 *              It provides a label as a content parameter.
 */
@Suppress("CognitiveComplexMethod", "LongParameterList")
@Composable
fun ExpirationField(
    onValueChange: (ValidatedExpirationDate?) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    textFieldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    initialValue: ValidatedExpirationDate = ValidatedExpirationDate(),
    separator: String = " / ",
    label: @Composable (() -> Unit)?,
) {
    var value by rememberSaveable(initialValue) {
        mutableStateOf(
            initialValue.getValidatedMonthAndYear()?.let { (month, year) ->
                "${setMonthString(month)}$year"
            } ?: "",
        )
    }
    val transformation = remember(separator) { ExpirationDateTransformation(separator) }

    @Suppress("MagicNumber")
    PaddableOutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { date ->
            value = date.filter { it.isDigit() }.take(4)
            if (value.length == 4) {
                try {
                    val month = value.take(2).toInt()
                    val year = value.substring(2).toInt()
                    onValueChange(ValidatedExpirationDate(month = month, year = year))
                } catch (e: NumberFormatException) {
                    onValueChange(null)
                }
            } else {
                onValueChange(null)
            }
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        visualTransformation = transformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
        contentPadding = textFieldPadding,
        singleLine = true,
    )
}

@Suppress("MagicNumber")
fun setMonthString(month: Int): String {
    return if (month < 10) {
        "%02d".format(Locale.US, month)
    } else {
        "$month"
    }
}

@Suppress("DataClassContainsFunctions")
data class ValidatedExpirationDate(
    private val month: Int? = null,
    private val year: Int? = null,
) {
    fun getValidatedMonthAndYear(): Pair<Int, Int>? =
        validatedMonthAndYear(month = month, year = year)
}
