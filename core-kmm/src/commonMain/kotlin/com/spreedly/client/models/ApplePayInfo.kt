package com.spreedly.client.models

import org.json.JSONObject

class ApplePayInfo(firstName: String, lastName: String, paymentData: String) : PaymentMethodInfo() {
    @kotlin.jvm.JvmField
    var testCardNumber: String? = null
    @kotlin.jvm.JvmField
    var paymentData: String

    init {
        this.firstName = firstName
        this.lastName = lastName
        this.paymentData = paymentData
    }

    public override fun toJson(): JSONObject {
        val wrapper = JSONObject()
        val paymentMethod = JSONObject()
        val applePay = JSONObject()
        addCommonJsonFields(paymentMethod, applePay)
        applePay.put("payment_data", paymentData)
        applePay.put("test_card_number", testCardNumber)
        paymentMethod.put("apple_pay", applePay)
        wrapper.put("payment_method", paymentMethod)
        return wrapper
    }
}