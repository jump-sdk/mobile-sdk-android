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
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spreedly.client.models.enums.CardBrand
import com.spreedly.client.models.enums.isValid
import com.spreedly.composewidgets.SecureCreditCardForm

class ComposeWidgetsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                MaterialTheme {
                    Surface(Modifier.fillMaxSize()) {
                        var isCardValid by remember { mutableStateOf(false) }
                        var brand by rememberSaveable { mutableStateOf(CardBrand.unknown) }

                        Column {
                            SecureCreditCardForm(
                                modifier = Modifier.padding(16.dp),
                                fieldModifier = Modifier.fillMaxWidth(),
                                onValidCreditCardInfo = { cardBrand, creditCardInfo ->
                                    brand = cardBrand
                                    Log.i(
                                        "ComposeWidgetsFragment",
                                        "Validated credit card info: $creditCardInfo",
                                    )
                                    isCardValid = creditCardInfo != null
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
