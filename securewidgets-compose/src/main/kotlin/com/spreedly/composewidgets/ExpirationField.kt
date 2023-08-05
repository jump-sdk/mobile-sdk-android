package com.spreedly.composewidgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun ExpirationField(
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier,
    separator: String = " / ",
    label: @Composable (() -> Unit)?,
) {
    var value by rememberSaveable { mutableStateOf("") }
    val transformation = ExpirationDateTransformation(separator)

    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { date ->
            val filtered = date.filter { it.isDigit() }
            when (filtered.length) {
                in 0..1 -> {
                    value = filtered
                }
                2 -> {
                    if (filtered.toInt() in 1..12) {
                        value = filtered
                    } else {
                        // noop
                    }
                }
                else -> {
                    try {
                        val year = filtered.substring(2)
                        when (year.length) {
                            1 -> {
                                if (year == "2") {
                                    value = filtered
                                }
                            }
                            2 -> {
                                if (year.toInt() >= 20) {
                                    value = filtered
                                }
                            }
                            3 -> {
                                if (year.toInt() >= 202) {
                                    value = filtered
                                }
                            }
                            4 -> {
                                if (year.toInt() >= 2023) {
                                    value = filtered
                                }
                            }
                            else -> {
                                // noop
                            }
                        }
                    } catch (e: NumberFormatException) {
                        // noop
                    }
                }
            }
            onValueChange(value)
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
