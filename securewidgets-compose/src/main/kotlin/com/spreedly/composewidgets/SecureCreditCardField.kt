package com.spreedly.composewidgets

import android.app.PendingIntent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.spreedly.client.SecureCreditCardNumber
import com.spreedly.client.cardBrand
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.maxNumberLength

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureCreditCardField(
    onValueChange: (SecureCreditCardNumber) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier = Modifier,
    recognitionIntent: PendingIntent? = null,
    cardRecognitionLauncher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>? = null,
    separator: String = " ",
    initialValue: String = "",
    label: @Composable() (() -> Unit)?,
) {
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }
    var transformation by remember(brand) {
        mutableStateOf(CreditCardNumberTransformation(brand, separator))
    }

    SecureTextField(
        modifier = modifier,
        autofill = AutofillType.CreditCardNumber,
        onValueChange = { cardNumber ->
            brand = cardNumber.cardBrand
            onValueChange(SecureCreditCardNumber(cardNumber))
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        maxValueLength = brand.maxNumberLength,
        visualTransformation = transformation,
        initialValue = initialValue,
        trailingIcon = {
            recognitionIntent?.let {
                IconButton(
                    onClick = {
                        cardRecognitionLauncher?.launch(
                            IntentSenderRequest.Builder(it.intentSender).build(),
                        )
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                        contentDescription = "Scan card",
                    )
                }
            }
        },
    )
}
