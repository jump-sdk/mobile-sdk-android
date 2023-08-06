package com.spreedly.composewidgets

import android.app.PendingIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.google.android.gms.wallet.WalletConstants
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.CreditCardInfoBuilder
import com.spreedly.client.models.SecureCreditCardNumber
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.composewidgets.utils.PaymentCardRecognition

/**
 * Composable function that creates a secure credit card input form for collecting credit card information.
 *
 * @param fieldSpacing The vertical spacing between the input fields.
 * @param modifier The modifier for the entire credit card form.
 * @param fieldModifier The modifier for individual input fields.
 * @param textStyle The text style to be applied to the input fields.
 * @param colors The colors customization for the input fields.
 * @param shape The shape customization for the input fields.
 * @param walletEnvironment The environment for OCR detection, should be a WalletConstants.ENVIRONMENT_* value.
 * @param labelFactory The composable function used to render the labels for input fields.
 *                      Takes a hint string as a parameter.
 * @param textFieldPadding The padding to be applied to the input fields.
 * @param onValidCreditCardInfo The callback function triggered when valid credit card information is provided.
 *                              It provides the detected card brand and credit card info as parameters.
 *                              Will be null if internal validation fails.
 */
@OptIn(ExperimentalMaterialApi::class)
@Suppress("LongMethod")
@Composable
fun SecureCreditCardForm(
    fieldSpacing: Dp,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    walletEnvironment: Int? = WalletConstants.ENVIRONMENT_TEST,
    labelFactory: @Composable (String) -> Unit = { Text(it) },
    textFieldPadding: PaddingValues = TextFieldDefaults.outlinedTextFieldPadding(),
    onValidCreditCardInfo: (CardBrand, CreditCardInfo?) -> Unit,
) {
    val context = LocalContext.current
    val creditCardInfoBuilder = rememberSaveable(
        saver = CreditCardInfoBuilderSaver,
    ) {
        CreditCardInfoBuilder()
    }
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }
    var recognitionIntent: PendingIntent? by remember(context) {
        mutableStateOf(null)
    }
    val cardChanged: (SecureCreditCardNumber) -> Unit = {
        brand = it.brand
        creditCardInfoBuilder.cardNumber = if (it.isValid) {
            it.number
        } else {
            null
        }
        onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
    }
    val expirationChanged: (ValidatedExpirationDate) -> Unit = { validatedDate ->
        validatedDate
            .getValidatedMonthAndYear()
            ?.let { (month, year) ->
                creditCardInfoBuilder.month = month
                creditCardInfoBuilder.year = year
            }
            ?: run {
                creditCardInfoBuilder.month = null
                creditCardInfoBuilder.year = null
            }
        onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
    }
    LaunchedEffect(context) {
        walletEnvironment?.let { env ->
            PaymentCardRecognition
                .getIntent(context, env)
                ?.let { recognitionIntent = it }
        }
    }
    var initialCardNumber by remember { mutableStateOf("") }
    var initialExpiration by remember { mutableStateOf(ValidatedExpirationDate()) }
    val cardRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
    ) {
        PaymentCardRecognition.fromResult(it)?.let { (cardNumber, expiration) ->
            initialCardNumber = cardNumber
            cardChanged(SecureCreditCardNumber(SpreedlySecureOpaqueString(cardNumber)))
            initialExpiration = expiration
            expirationChanged(expiration)
        }
    }

    Column(modifier) {
        NameField(
            modifier = fieldModifier,
            onValueChange = {
                creditCardInfoBuilder.fullName = if (it.isNotBlank()) {
                    it
                } else {
                    null
                }
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.card_name_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            textFieldPadding = textFieldPadding,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureCreditCardField(
            modifier = fieldModifier,
            onValueChange = cardChanged,
            label = { labelFactory(stringResource(id = R.string.card_number_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            initialValue = initialCardNumber,
            recognitionIntent = recognitionIntent,
            cardRecognitionLauncher = cardRecognitionLauncher,
            textFieldPadding = textFieldPadding,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureVerificationNumberField(
            modifier = fieldModifier,
            onValueChange = { number, isValid ->
                creditCardInfoBuilder.cvc = if (isValid) {
                    number
                } else {
                    null
                }
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.cvc_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            textFieldPadding = textFieldPadding,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        ExpirationField(
            modifier = fieldModifier,
            onValueChange = expirationChanged,
            label = { labelFactory(stringResource(id = R.string.expiration_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
            initialValue = initialExpiration,
            textFieldPadding = textFieldPadding,
        )
    }
}

// ktlint-disable experimental:property-naming
val CreditCardInfoBuilderSaver = listSaver(
    save = {
        listOf(
            it.fullName,
            it.cardNumber?._encode(),
            it.cvc?._encode(),
            it.month,
            it.year,
        )
    },
    restore = { valueList ->
        CreditCardInfoBuilder().apply {
            fullName = valueList[0] as? String
            cardNumber = (valueList[1] as? String)?.let { SpreedlySecureOpaqueString(it) }
            cvc = (valueList[2] as? String)?.let { SpreedlySecureOpaqueString(it) }
            month = valueList[2] as? Int
            year = valueList[3] as? Int
        }
    },
)
