package com.spreedly.composewidgets

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.spreedly.client.models.SpreedlySecureOpaqueString

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureTextField(
    modifier: Modifier,
    autofill: AutofillType,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    maxValueLength: Int,
    visualTransformation: VisualTransformation,
    onValueChange: (SpreedlySecureOpaqueString) -> Unit,
    label: @Composable (() -> Unit)?,
) {
    var value by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier.autofill(
            autofillTypes = listOf(autofill),
            onFill = {
                value = it
                onValueChange(SpreedlySecureOpaqueString(value))
            },
        ),
        value = value,
        onValueChange = { number ->
            value = number.filter { it.isDigit() }.take(maxValueLength)
            onValueChange(SpreedlySecureOpaqueString(value))
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number,
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
    )
}