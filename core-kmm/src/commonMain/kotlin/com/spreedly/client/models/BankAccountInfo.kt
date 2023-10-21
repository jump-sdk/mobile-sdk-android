package com.spreedly.client.models

import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import kotlinx.serialization.json.JsonObject

@Suppress("LongParameterList")
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
    override fun toJson(): JsonObject {
        val request = generateBaseRequestMap()
        val bankAccount = generatePersonInfoMap()
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
        request["bank_account"] = JsonObject(bankAccount)
        return JsonObject(
            mapOf("payment_method" to JsonObject(request)),
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BankAccountInfo

        if (routingNumber != other.routingNumber) return false
        if (accountNumber != other.accountNumber) return false
        if (accountType != other.accountType) return false
        return accountHolderType == other.accountHolderType
    }

    override fun hashCode(): Int {
        var result = routingNumber.hashCode()
        result = 31 * result + accountNumber.hashCode()
        result = 31 * result + (accountType?.hashCode() ?: 0)
        result = 31 * result + (accountHolderType?.hashCode() ?: 0)
        return result
    }
}
