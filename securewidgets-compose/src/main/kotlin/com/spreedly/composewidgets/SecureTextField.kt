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
import androidx.compose.ui.autofill.AutofillNode
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.composed
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalAutofill
import androidx.compose.ui.platform.LocalAutofillTree
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import com.spreedly.client.models.SpreedlySecureOpaqueString

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureTextField(
    modifier: Modifier,
    autofill: AutofillType,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    onValueChange: (SpreedlySecureOpaqueString) -> Unit,
    label: @Composable (() -> Unit)?,
) {
    var value by rememberSaveable { mutableStateOf("") }
    OutlinedTextField(
        modifier = modifier.autofill(
            autofillTypes = listOf(autofill),
            onFill = { value = it },
        ),
        value = value,
        onValueChange = { number ->
            value = number.filter { it.isDigit() }
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
    )
}

@OptIn(ExperimentalComposeUiApi::class)
private fun Modifier.autofill(autofillTypes: List<AutofillType>, onFill: ((String) -> Unit)) = composed {
    val autofill = LocalAutofill.current
    val autofillNode = AutofillNode(onFill = onFill, autofillTypes = autofillTypes)
    LocalAutofillTree.current += autofillNode

    this.onGloballyPositioned {
        autofillNode.boundingBox = it.boundsInWindow()
    }.onFocusChanged { focusState ->
        autofill?.run {
            if (focusState.isFocused) {
                requestAutofillForNode(autofillNode)
            } else {
                cancelAutofillForNode(autofillNode)
            }
        }
    }
}
