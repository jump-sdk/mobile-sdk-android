package com.spreedly.composewidgets

import androidx.compose.runtime.saveable.listSaver
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.CreditCardInfoBuilder
import com.spreedly.client.models.SpreedlySecureOpaqueString

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
            @Suppress("DontForceCast")
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

// ktlint-disable experimental:property-naming
val CreditCardInfoBuilderSaver = listSaver<CreditCardInfoBuilder, Any?>(
    save = {
        listOf(
            it.fullName,
            it.cardNumber?._encode(),
            it.cvc?._encode(),
            it.month,
            it.year,
            it.postalCode,
            it.retained,
        )
    },
    restore = { valueList ->
        CreditCardInfoBuilder().apply {
            fullName = valueList[0] as? String
            cardNumber = (valueList[1] as? String)?.let { SpreedlySecureOpaqueString(it) }
            cvc = (valueList[2] as? String)?.let { SpreedlySecureOpaqueString(it) }
            month = valueList[3] as? Int
            year = valueList[4] as? Int
            postalCode = valueList[5] as? String
            retained = valueList[6] as? Boolean
        }
    },
)
