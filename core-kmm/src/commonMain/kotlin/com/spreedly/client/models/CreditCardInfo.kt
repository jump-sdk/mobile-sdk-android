package com.spreedly.client.models

import org.json.JSONObject

class CreditCardInfo : PaymentMethodInfo {
    @kotlin.jvm.JvmField
    var number: SpreedlySecureOpaqueString? = null
    @kotlin.jvm.JvmField
    var verificationValue: SpreedlySecureOpaqueString? = null
    @kotlin.jvm.JvmField
    var month = 0
    @kotlin.jvm.JvmField
    var year = 0
    @kotlin.jvm.JvmField
    var allowBlankName: Boolean? = null
    @kotlin.jvm.JvmField
    var allowExpiredDate: Boolean? = null
    @kotlin.jvm.JvmField
    var allowBlankDate: Boolean? = null
    @kotlin.jvm.JvmField
    var eligibleForCardUpdate: Boolean? = null

    constructor(copy: PaymentMethodInfo) : super(copy) {
        if (copy.javaClass == CreditCardInfo::class.java) {
            val ccCopy = copy as CreditCardInfo
            allowBlankName = ccCopy.allowBlankName
            allowBlankDate = ccCopy.allowBlankDate
            allowExpiredDate = ccCopy.allowExpiredDate
            eligibleForCardUpdate = ccCopy.eligibleForCardUpdate
            month = ccCopy.month
            year = ccCopy.year
        }
    }

    constructor(
        fullName: String?,
        number: SpreedlySecureOpaqueString?,
        verificationValue: SpreedlySecureOpaqueString?,
        year: Int,
        month: Int
    ) {
        this.fullName = fullName
        this.number = number
        this.verificationValue = verificationValue
        this.month = month
        this.year = year
    }

    constructor(
        firstName: String?,
        lastName: String?,
        number: SpreedlySecureOpaqueString?,
        verificationValue: SpreedlySecureOpaqueString?,
        year: Int,
        month: Int
    ) {
        this.firstName = firstName
        this.lastName = lastName
        this.number = number
        this.verificationValue = verificationValue
        this.month = month
        this.year = year
    }

    constructor() {}

    public override fun toJson(): JSONObject {
        val wrapper = JSONObject()
        val paymentMethod = JSONObject()
        val creditCard = JSONObject()
        addCommonJsonFields(paymentMethod, creditCard)
        creditCard.put("verification_value", verificationValue!!._encode())
        creditCard.put("number", number!!._encode())
        creditCard.put("month", month)
        creditCard.put("year", year)
        paymentMethod.put("credit_card", creditCard)
        paymentMethod.put("allow_blank_name", allowBlankName)
        paymentMethod.put("allow_expired_date", allowExpiredDate)
        paymentMethod.put("allow_blank_date", allowBlankDate)
        paymentMethod.put("eligible_for_card_updater", eligibleForCardUpdate)
        wrapper.put("payment_method", paymentMethod)
        return wrapper
    }
}