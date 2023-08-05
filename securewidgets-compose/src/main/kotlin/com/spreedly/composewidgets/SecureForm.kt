package com.spreedly.composewidgets

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun SecureForm(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    fieldSpacing: Dp = 16.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    labelFactory: @Composable (String) -> Unit = { Text(it) },
) {
    Column(modifier) {
        NameField(
            modifier = fieldModifier,
            onValueChange = { Log.i("ComposeWidgetsFragment", "name: $it") },
            label = { labelFactory(stringResource(id = R.string.card_name_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureCreditCardField(
            modifier = fieldModifier,
            onValueChange = {
                Log.i("ComposeWidgetsFragment", "credit card: $it")
            },
            label = { labelFactory(stringResource(id = R.string.card_number_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureVerificationNumberField(
            modifier = fieldModifier,
            onValueChange = { number, isValid ->
                Log.i("ComposeWidgetsFragment", "cvv: $number, isValid: $isValid")
            },
            label = { labelFactory(stringResource(id = R.string.cvc_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        ExpirationField(
            modifier = fieldModifier,
            onValueChange = {
                Log.i("ComposeWidgetsFragment", "expiration: $it")
            },
            label = { labelFactory(stringResource(id = R.string.expiration_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
    }
}
