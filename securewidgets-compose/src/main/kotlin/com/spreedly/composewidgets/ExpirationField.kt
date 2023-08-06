package com.spreedly.composewidgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
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

@Suppress("CognitiveComplexMethod")
@Composable
fun ExpirationField(
    onValueChange: (ValidatedExpirationDate) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier,
    initialValue: ValidatedExpirationDate = ValidatedExpirationDate(),
    separator: String = " / ",
    label: @Composable (() -> Unit)?,
) {
    var value by rememberSaveable(initialValue) {
        mutableStateOf(
            initialValue.getValidatedMonthAndYear()?.let { (month, year) ->
                "%02d%d".format(month, year)
            } ?: "",
        )
    }
    val transformation = remember(separator) { ExpirationDateTransformation(separator) }

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { date ->
            value = date.filter { it.isDigit() }
            try {
                val month = value.take(2).toInt()
                val year = value.substring(2).toInt()
                onValueChange(ValidatedExpirationDate(month = month, year = year))
            } catch (e: NumberFormatException) {
                onValueChange(ValidatedExpirationDate())
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
    )
}

@Suppress("DataClassContainsFunctions")
data class ValidatedExpirationDate(
    private val month: Int? = null,
    private val year: Int? = null,
) {
    fun getValidatedMonthAndYear(): Pair<Int, Int>? {
        @Suppress("ComplexCondition", "MagicNumber")
        return if (month != null && month in 1..12 && year != null && year >= 2023) {
            month to year
        } else {
            null
        }
    }
}
