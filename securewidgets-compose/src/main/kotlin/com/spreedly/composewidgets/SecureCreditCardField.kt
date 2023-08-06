package com.spreedly.composewidgets

import android.app.Activity
import android.app.PendingIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import com.google.android.gms.wallet.PaymentCardRecognitionResult
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.client.models.enums.maxNumberLength
import com.spreedly.client.models.enums.validateNumberLength

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SecureCreditCardField(
    onValueChange: (SecureCreditCardNumber) -> Unit,
    textStyle: TextStyle,
    shape: Shape,
    colors: TextFieldColors,
    modifier: Modifier = Modifier,
    recognitionIntent: PendingIntent? = null,
    separator: String = " ",
    label: @Composable() (() -> Unit)?,
) {
    val context = LocalContext.current
    val cardRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) {
        when (it.resultCode) {
            Activity.RESULT_OK -> {
                it.data
                    ?.let { intent ->
                        PaymentCardRecognitionResult.getFromIntent(intent)
                    }
                    ?.let { result ->
                        val creditCardExpirationDate = result.creditCardExpirationDate
                        val expirationDate = creditCardExpirationDate?.let {
                            "%02d/%d".format(it.month, it.year)
                        }
                        val cardResultText = "PAN: ${result.pan}\nExpiration date: $expirationDate"
                        println(expirationDate)
                        println(cardResultText)
                    }
            }
            Activity.RESULT_CANCELED -> {
                println("card recognition canceled")
            }
        }
    }
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }
    var transformation by remember(brand) {
        mutableStateOf(CreditCardNumberTransformation(brand, separator))
    }

    SecureTextField(
        modifier = modifier,
        autofill = AutofillType.CreditCardNumber,
        onValueChange = { cardNumber ->
            brand = cardNumber.cardBrand
            onValueChange(
                SecureCreditCardNumber(
                    number = cardNumber,
                    isValid = brand.isValid && brand.validateNumberLength(cardNumber._encode()),
                    brand = brand,
                ),
            )
        },
        label = label,
        textStyle = textStyle,
        shape = shape,
        colors = colors,
        maxValueLength = brand.maxNumberLength,
        visualTransformation = transformation,
        trailingIcon = {
            recognitionIntent?.let {
                IconButton(
                    onClick = {
                        cardRecognitionLauncher.launch(
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

data class SecureCreditCardNumber(
    val number: SpreedlySecureOpaqueString,
    val isValid: Boolean,
    val brand: CardBrand,
)

private val SpreedlySecureOpaqueString.cardBrand: CardBrand
    get() = if (this._encode().length < 16) {
        this.softDetect()
    } else {
        this.detectCardType()
    }
