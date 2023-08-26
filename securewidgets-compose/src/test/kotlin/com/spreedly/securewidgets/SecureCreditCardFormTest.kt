package com.spreedly.securewidgets

import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import app.cash.paparazzi.DeviceConfig
import app.cash.paparazzi.Paparazzi
import com.spreedly.composewidgets.SecureCreditCardForm
import org.junit.Rule
import org.junit.Test

class SecureCreditCardFormTest {
    @get: Rule
    val paparazziRule = Paparazzi(
        deviceConfig = DeviceConfig.PIXEL_6,
    )

    @Test
    fun SecureCreditCardFormSnapshot() {
        paparazziRule.snapshot {
            MaterialTheme {
                LocalActivityResultRegistryOwnerProvider {
                    SecureCreditCardForm(
                        fieldSpacing = 14.dp,
                    ) { _, _ ->
                    }
                }
            }
        }
    }

    @Composable
    private fun LocalActivityResultRegistryOwnerProvider(content: @Composable () -> Unit) {
        CompositionLocalProvider(
            LocalActivityResultRegistryOwner provides object : ActivityResultRegistryOwner {
                override val activityResultRegistry: ActivityResultRegistry =
                    object : ActivityResultRegistry() {
                        override fun <I : Any?, O : Any?> onLaunch(
                            requestCode: Int,
                            contract: ActivityResultContract<I, O>,
                            input: I,
                            options: ActivityOptionsCompat?,
                        ) {
                            TODO("Not yet implemented")
                        }
                    }
            },
            content = content,
        )
    }
}
