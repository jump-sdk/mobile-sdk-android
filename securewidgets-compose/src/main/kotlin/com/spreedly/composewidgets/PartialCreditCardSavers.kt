package com.spreedly.composewidgets

import androidx.compose.runtime.saveable.listSaver
import com.spreedly.client.models.PartialCreditCardInfo
import com.spreedly.client.models.PartialCreditCardInfoBuilder

// ktlint-disable experimental:property-naming
val PartialCreditCardInfoSaver = listSaver<PartialCreditCardInfo?, Any?>(
    save = {
        it?.let {
            listOf(
                it.fullName,
                it.month,
                it.year,
                it.postalCode,
            )
        } ?: emptyList<Any>()
    },
    restore = { valueList ->
        if (valueList.size != 7) {
            null
        } else {
            @Suppress("DontForceCast")
            PartialCreditCardInfo(
                fullName = valueList[0] as String,
                month = valueList[1] as Int,
                year = valueList[2] as Int,
                postalCode = valueList[3] as String,
                streetAddress = valueList[4] as String,
                city = valueList[5] as String,
                state = valueList[6] as String,
            )
        }
    },
)

// ktlint-disable experimental:property-naming
val PartialCreditCardInfoBuilderSaver = listSaver<PartialCreditCardInfoBuilder, Any?>(
    save = {
        listOf(
            it.fullName,
            it.month,
            it.year,
            it.postalCode,
        )
    },
    restore = { valueList ->
        PartialCreditCardInfoBuilder().apply {
            fullName = valueList[0] as? String
            month = valueList[1] as? Int
            year = valueList[2] as? Int
            postalCode = valueList[3] as? String
        }
    },
)
