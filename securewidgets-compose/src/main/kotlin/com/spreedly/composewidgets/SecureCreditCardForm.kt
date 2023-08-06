package com.spreedly.composewidgets

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
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.CreditCardInfoBuilder
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand

@Suppress("LongMethod")
@Composable
fun SecureCreditCardForm(
    fieldSpacing: Dp,
    modifier: Modifier = Modifier,
    fieldModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,
    colors: TextFieldColors = TextFieldDefaults.outlinedTextFieldColors(),
    shape: Shape = MaterialTheme.shapes.small,
    labelFactory: @Composable (String) -> Unit = { Text(it) },
    onValidCreditCardInfo: (CardBrand, CreditCardInfo?) -> Unit,
) {
    val creditCardInfoBuilder = rememberSaveable(
        saver = CreditCardInfoBuilderSaver,
    ) {
        CreditCardInfoBuilder()
    }
    var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }

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
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        SecureCreditCardField(
            modifier = fieldModifier,
            onValueChange = {
                brand = it.brand
                println(it)
                creditCardInfoBuilder.cardNumber = if (it.isValid) {
                    it.number
                } else {
                    null
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
        )
        Spacer(modifier = Modifier.height(fieldSpacing))
        ExpirationField(
            modifier = fieldModifier,
            onValueChange = { validatedDate ->
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
            },
            label = { labelFactory(stringResource(id = R.string.expiration_hint)) },
            textStyle = textStyle,
            colors = colors,
            shape = shape,
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
