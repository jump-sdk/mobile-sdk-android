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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.CreditCardInfoBuilder
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand

@Composable
fun SecureCreditCardForm(
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    fieldSpacing: Dp = 16.dp,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    labelFactory: @Composable (String) -> Unit = { Text(it) },
    onValidCreditCardInfo: (CardBrand, CreditCardInfo?) -> Unit,
) {
    var creditCardInfoBuilder = rememberSaveable(
        saver = CreditCardInfoBuilderSaver,
    ) {
        CreditCardInfoBuilder()
    }
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }

    Column(modifier) {
        NameField(
            modifier = fieldModifier,
            onValueChange = {
                creditCardInfoBuilder = if (it.isNotBlank()) {
                    creditCardInfoBuilder.copy(fullName = it)
                } else {
                    creditCardInfoBuilder.copy(fullName = null)
                }
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.card_name_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureCreditCardField(
            modifier = fieldModifier,
            onValueChange = {
                brand = it.brand
                creditCardInfoBuilder = if (it.isValid) {
                    creditCardInfoBuilder.copy(cardNumber = it.number)
                } else {
                    creditCardInfoBuilder.copy(cardNumber = null)
                }
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
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
                creditCardInfoBuilder = if (isValid) {
                    creditCardInfoBuilder.copy(cvc = number)
                } else {
                    creditCardInfoBuilder.copy(cvc = null)
                }
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.cvc_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        ExpirationField(
            modifier = fieldModifier,
            onValueChange = { validatedDate ->
                Log.i("ComposeWidgetsFragment", "expiration: $validatedDate")
                creditCardInfoBuilder = validatedDate
                    .getValidatedMonthAndYear()
                    ?.let { (month, year) ->
                        creditCardInfoBuilder.copy(month = month, year = year)
                    } ?: creditCardInfoBuilder.copy(month = null, year = null)
                onValidCreditCardInfo(brand, creditCardInfoBuilder.build())
            },
            label = { labelFactory(stringResource(id = R.string.expiration_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
        )
    }
}

// ktlint-disable experimental:property-naming
private val CreditCardInfoBuilderSaver = listSaver(
    save = { listOf(it.fullName, it.cardNumber?._encode(), it.cvc?._encode(), it.month, it.year) },
    restore = { valueList ->
        CreditCardInfoBuilder(
            fullName = valueList[0] as? String,
            cardNumber = (valueList[1] as? String)?.let { SpreedlySecureOpaqueString(it) },
            cvc = (valueList[2] as? String)?.let { SpreedlySecureOpaqueString(it) },
            month = valueList[2] as? Int,
            year = valueList[3] as? Int,
        )
    },
)
