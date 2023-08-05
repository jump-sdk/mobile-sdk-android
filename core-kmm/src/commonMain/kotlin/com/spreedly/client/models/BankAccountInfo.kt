package com.spreedly.client.models

import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class BankAccountInfo(
    firstName: String?,
    lastName: String?,
    fullName: String?,
    val routingNumber: String,
    val accountNumber: SpreedlySecureOpaqueString,
    val accountType: AccountType?,
    val accountHolderType: AccountHolderType? = null,
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
        require(!fullName.isNullOrBlank() || (!firstName.isNullOrBlank() && !lastName.isNullOrBlank())) {
            "Either fullName or firstName and lastName must be provided"
        }
    }

    override fun toJson(): JsonObject {
        val paymentMethod = mutableMapOf<String, JsonElement>()
        val bankAccount = mutableMapOf<String, JsonElement>()
        addCommonJsonFields(paymentMethod, bankAccount)
        bankAccount.putAsJsonElement("bank_routing_number", routingNumber)
        bankAccount.putAsJsonElement("bank_account_number", accountNumber._encode())
        accountType?.also {
            bankAccount.putAsJsonElement("bank_account_type", it.toString().lowercase())
        } ?: run {
            bankAccount.putAsJsonElement("bank_account_type", "")
        }
        accountHolderType?.let {
            bankAccount.putAsJsonElement("bank_account_holder_type", it.toString().lowercase())
        }
        paymentMethod.put("bank_account", JsonObject(bankAccount))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
