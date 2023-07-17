package com.spreedly.client.models

import com.spreedly.client.models.enums.AccountHolderType
import com.spreedly.client.models.enums.AccountType
import org.json.JSONObject

class BankAccountInfo : PaymentMethodInfo {
    @kotlin.jvm.JvmField
    var routingNumber: String? = null
    @kotlin.jvm.JvmField
    var accountNumber: SpreedlySecureOpaqueString? = null
    @kotlin.jvm.JvmField
    var accountType: AccountType? = null
    @kotlin.jvm.JvmField
    var accountHolderType: AccountHolderType? = null

    constructor(copy: PaymentMethodInfo) : super(copy) {
        if (copy.javaClass == BankAccountInfo::class.java) {
            val baCopy = copy as BankAccountInfo
            accountType = baCopy.accountType
            accountHolderType = baCopy.accountHolderType
        }
    }

    constructor() {}
    constructor(
        fullName: String?,
        routingNumber: String?,
        accountNumber: SpreedlySecureOpaqueString?,
        accountType: AccountType?
    ) {
        this.fullName = fullName
        this.routingNumber = routingNumber
        this.accountNumber = accountNumber
        this.accountType = accountType
    }

    constructor(
        firstName: String?,
        lastName: String?,
        routingNumber: String?,
        accountNumber: SpreedlySecureOpaqueString?,
        accountType: AccountType?
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.routingNumber = routingNumber
        this.accountNumber = accountNumber
        this.accountType = accountType
    }

    public override fun toJson(): JSONObject {
        val wrapper = JSONObject()
        val paymentMethod = JSONObject()
        val bankAccount = JSONObject()
        addCommonJsonFields(paymentMethod, bankAccount)
        bankAccount.put("bank_routing_number", routingNumber)
        bankAccount.put("bank_account_number", accountNumber!!._encode())
        try {
            bankAccount.put("bank_account_type", accountType.toString().lowercase())
        } catch (e: NullPointerException) {
            bankAccount.put("bank_account_type", "")
        }
        try {
            bankAccount.put("bank_account_holder_type", accountHolderType.toString().lowercase())
        } catch (e: NullPointerException) {
        }
        paymentMethod.put("bank_account", bankAccount)
        wrapper.put("payment_method", paymentMethod)
        return wrapper
    }
}