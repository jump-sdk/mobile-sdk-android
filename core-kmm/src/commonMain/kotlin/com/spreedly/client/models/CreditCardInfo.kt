package com.spreedly.client.models

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
    fun prevalidate(): Boolean = validatedMonthAndYear(month = month, year = year) != null &&
        number.isValidCreditCard &&
        verificationValue.isValidCvc(number.cardBrand) &&
        (
            !fullName.isNullOrBlank() xor
                (!firstName.isNullOrBlank() && !lastName.isNullOrBlank())
            )

    override fun toJson(): JsonObject {
        val request = generateBaseRequestMap()
        val creditCard = generatePersonInfoMap()
        creditCard.putAsJsonElement("verification_value", verificationValue._encode())
        creditCard.putAsJsonElement("number", number._encode())
        creditCard.putAsJsonElement("month", month)
        creditCard.putAsJsonElement("year", year)
        request["credit_card"] = JsonObject(creditCard)
        return JsonObject(
            mapOf("payment_method" to JsonObject(request)),
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
