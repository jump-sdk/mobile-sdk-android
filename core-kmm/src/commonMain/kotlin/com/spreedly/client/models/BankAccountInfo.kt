package com.spreedly.client.models

import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class BankAccountInfo(
    firstName: String,
    lastName: String,
    val routingNumber: String,
    val accountNumber: SpreedlySecureOpaqueString,
    val accountType: AccountType?,
    val accountHolderType: AccountHolderType? = null,
    retained: Boolean? = null,
) : PaymentMethodInfo(
    firstName = firstName,
    lastName = lastName,
    retained = retained,
) {

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
