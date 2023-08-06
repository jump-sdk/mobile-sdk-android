package com.spreedly.sdk_sample.widget // ktlint-disable package-name

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spreedly.client.SpreedlyClient
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.composewidgets.SecureCreditCardForm
import kotlinx.coroutines.launch

class ComposeWidgetsFragment : Fragment() {
    private val spreedlyClient by lazy {
        SpreedlyClient.newInstance(
            envKey = "XsQXqPtrgCOnpexSwyhzN9ngr2c",
            envSecret = "ghEGueczUT4BhJv54K24G6B4Oy9yWaM5R4dR2yt5gRsx3xnwbZE0OZ0mRg2zyI5g",
            test = true,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface(Modifier.fillMaxSize()) {
                        val coroutineScope = rememberCoroutineScope()

                        var isCardValid by rememberSaveable { mutableStateOf(false) }
                        var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }
                        var cardInfo = rememberSaveable<CreditCardInfo?>(
                            stateSaver = CreditCardInfoSaver,
                        ) { mutableStateOf(null) }
                        var token by remember { mutableStateOf("") }
                        var error by remember { mutableStateOf("") }

                        Column {
                            SecureCreditCardForm(
                                modifier = Modifier.padding(16.dp),
                                fieldModifier = Modifier.fillMaxWidth(),
                                fieldSpacing = 16.dp,
                                onValidCreditCardInfo = { cardBrand, creditCardInfo ->
                                    token = ""
                                    error = ""
                                    brand = cardBrand
                                    Log.i(
                                        "ComposeWidgetsFragment",
                                        "Validated credit card info: $creditCardInfo",
                                    )
                                    isCardValid = creditCardInfo != null
                                    cardInfo.value = creditCardInfo
                                },
                            )
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "card brand: $brand",
                                color = if (brand.isValid) Color.Black else Color.Red,
                            )
                            Text(
                                modifier = Modifier.padding(16.dp),
                                text = "info complete and valid: $isCardValid",
                                color = if (isCardValid) Color.Black else Color.Red,
                            )
                            Button(
                                enabled = isCardValid && token.isEmpty() && error.isEmpty(),
                                onClick = {
                                    Log.i(
                                        "ComposeWidgetsFragment",
                                        "Submitting credit card info...",
                                    )
                                    token = ""
                                    error = ""
                                    coroutineScope.launch {
                                        Log.i(
                                            "ComposeWidgetsFragment",
                                            "Launching request...",
                                        )
                                        val result = try {
                                            cardInfo.value?.let {
                                                spreedlyClient.createCreditCardPaymentMethod(it)
                                            }
                                        } catch (e: Exception) {
                                            Log.e(
                                                "ComposeWidgetsFragment",
                                                "Error creating payment method: ${e.message}",
                                                e,
                                            )
                                            null
                                        }
                                        Log.i(
                                            "ComposeWidgetsFragment",
                                            result.toString(),
                                        )
                                        result?.token?.let {
                                            token = it
                                        }
                                        result?.errors?.firstOrNull()?.let {
                                            error = it.toString()
                                        }
                                    }
                                },
                            ) {
                                Text("Submit")
                            }
                            if (token.isNotEmpty()) {
                                Text(text = "Token: $token", color = Color.Green)
                            }
                            if (error.isNotEmpty()) {
                                Text(text = "error: $error", color = Color.Red)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(): ComposeWidgetsFragment {
            return ComposeWidgetsFragment()
        }
    }
}

// ktlint-disable experimental:property-naming

val CreditCardInfoSaver = listSaver<CreditCardInfo?, Any?>(
    save = {
        it?.let {
            listOf<Any?>(
                it.firstName,
                it.lastName,
                it.fullName,
                it.number._encode(),
                it.verificationValue._encode(),
                it.month,
                it.year,
                it.retained,
            )
        } ?: emptyList<Any>()
    },
    restore = { valueList ->
        if (valueList.size != 8) {
            null
        } else {
            CreditCardInfo(
                firstName = valueList[0] as? String,
                lastName = valueList[1] as? String,
                fullName = valueList[2] as? String,
                number = SpreedlySecureOpaqueString(valueList[3] as String),
                verificationValue = SpreedlySecureOpaqueString(valueList[4] as String),
                month = valueList[5] as Int,
                year = valueList[6] as Int,
                retained = valueList[7] as? Boolean,
            )
        }
    },
)
