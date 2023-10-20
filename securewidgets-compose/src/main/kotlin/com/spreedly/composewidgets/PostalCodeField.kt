package com.spreedly.composewidgets

import androidx.compose.foundation.layout.PaddingValues
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
import com.spreedly.composewidgets.utils.PaddableOutlinedTextField
import com.spreedly.composewidgets.utils.autofill

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PostalCodeField(
    onValueChange: (String) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    textFieldPadding: PaddingValues,
    modifier: Modifier = Modifier,
    initialValue: String = "",
    label: @Composable () -> Unit,
) {
    var value by rememberSaveable { mutableStateOf(initialValue) }

    PaddableOutlinedTextField(
        modifier = modifier.autofill(
            autofillTypes = listOf(AutofillType.PostalCode),
            onFill = {
                value = it
                onValueChange(it)
            },
        ),
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        contentPadding = textFieldPadding,
        singleLine = true,
    )
}
