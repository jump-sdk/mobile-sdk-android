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
    retained: Boolean = false,
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
        try {
            bankAccount.putAsJsonElement("bank_account_type", accountType.toString().lowercase())
        } catch (e: NullPointerException) {
            bankAccount.putAsJsonElement("bank_account_type", "")
        }
        try {
            bankAccount.putAsJsonElement("bank_account_holder_type", accountHolderType.toString().lowercase())
        } catch (e: NullPointerException) {
        }
        paymentMethod.put("bank_account", JsonObject(bankAccount))
        return JsonObject(
            mapOf("payment_method" to JsonObject(paymentMethod)),
        )
    }
}
