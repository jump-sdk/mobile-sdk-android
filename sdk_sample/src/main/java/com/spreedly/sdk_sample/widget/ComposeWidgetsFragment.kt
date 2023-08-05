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
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import com.spreedly.composewidgets.SecureCreditCardField

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
                        Column(Modifier.padding(16.dp)) {
                            SecureCreditCardField(
                                modifier = Modifier.fillMaxWidth(),
                                onValueChange = {
                                    Log.i("ComposeWidgetsFragment", "onValueChange: $it")
                                },
                                label = null,
                                textStyle = LocalTextStyle.current,
                                colors = TextFieldDefaults.outlinedTextFieldColors(),
                                shape = MaterialTheme.shapes.small,
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
