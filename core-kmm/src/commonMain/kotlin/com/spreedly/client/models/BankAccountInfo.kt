package com.spreedly.client.models

import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

class BankAccountInfo(
    private val routingNumber: String? = null,
    private val accountNumber: SpreedlySecureOpaqueString,
    private val accountType: AccountType? = null,
    private val accountHolderType: AccountHolderType? = null,
) : PaymentMethodInfo() {

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
