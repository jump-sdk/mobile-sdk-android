package com.spreedly.securewidgets

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
import com.spreedly.client.models.SpreedlySecureOpaqueString

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureTextField(
    modifier: Modifier = Modifier,
    autofill: AutofillType,
    onValueChange: (SpreedlySecureOpaqueString) -> Unit,
    label: @Composable (() -> Unit)? = null,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    keyboardOptions: KeyboardOptions,
) {
    var value by rememberSaveable {
        mutableStateOf("")
    }
    OutlinedTextField(
        modifier = modifier.autofill(
            autofillTypes = listOf(autofill),
            onFill = { value = it },
        ),
        value = value,
        onValueChange = { onValueChange(SpreedlySecureOpaqueString(it)) },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        keyboardOptions = keyboardOptions,
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
