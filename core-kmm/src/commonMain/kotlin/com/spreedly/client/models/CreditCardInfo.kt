package com.spreedly.client.models

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class CreditCardInfo(
    firstName: String?,
    lastName: String?,
    fullName: String?,
    val number: SpreedlySecureOpaqueString,
    val verificationValue: SpreedlySecureOpaqueString,
    val month: Int = 0,
    val year: Int = 0,
    retained: Boolean? = null,
    address: Address? = null,
    shippingAddress: Address? = null,
) : PaymentMethodInfo(
    firstName = firstName,
    lastName = lastName,
    fullName = fullName,
    retained = retained,
    address = address,
    shippingAddress = shippingAddress,
) {
    init {
        require(year >= 2023) { "Year must be 2023 or later" }
        require(month in 1..12) { "Month must be between 1 and 12" }
        require(
            !fullName.isNullOrBlank() ||
                (!firstName.isNullOrBlank() && !lastName.isNullOrBlank()),
        ) {
            "Either fullName or firstName and lastName must be provided"
        }
    }

    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val creditCard = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, creditCard)
        creditCard.putAsJsonElement("verification_value", verificationValue._encode())
        creditCard.putAsJsonElement("number", number._encode())
        creditCard.putAsJsonElement("month", month)
        creditCard.putAsJsonElement("year", year)
        paymentMethod.put("credit_card", JsonObject(creditCard))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreditCardInfo

        if (number != other.number) return false
        if (verificationValue != other.verificationValue) return false
        if (month != other.month) return false
        if (year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = number.hashCode()
        result = 31 * result + verificationValue.hashCode()
        result = 31 * result + month
        result = 31 * result + year
        return result
    }
}
