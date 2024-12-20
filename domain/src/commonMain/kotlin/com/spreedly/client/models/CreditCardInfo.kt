package com.spreedly.client.models

import kotlinx.serialization.json.JsonObject

data class CreditCardInfo(
    override val firstName: String?,
    override val lastName: String?,
    override val fullName: String?,
    val number: SpreedlySecureOpaqueString,
    val verificationValue: SpreedlySecureOpaqueString,
    val month: Int,
    val year: Int,
    override val retained: Boolean? = null,
    override val address: Address? = null,
    override val shippingAddress: Address? = null,
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
}
