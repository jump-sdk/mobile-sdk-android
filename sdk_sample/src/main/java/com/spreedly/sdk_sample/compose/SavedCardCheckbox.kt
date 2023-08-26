package com.spreedly.sdk_sample.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SaveCardCheckbox(onCheckedChange: (Boolean) -> Unit) {
    var isSetAsDefault by rememberSaveable {
        mutableStateOf(false)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val isDefault = !isSetAsDefault
                isSetAsDefault = isDefault
                onCheckedChange(isDefault)
            }
            .padding(bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = isSetAsDefault,
            onCheckedChange = { isDefault ->
                isSetAsDefault = isDefault
                onCheckedChange(isDefault)
            },
        )
        Text(text = "Save card for future purchases")
    }
}
