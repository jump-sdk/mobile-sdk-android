package com.spreedly.client.models

import org.json.JSONObject

class GooglePayInfo(firstName: String, lastName: String, paymentData: String, retained: Boolean) :
    PaymentMethodInfo() {
    @kotlin.jvm.JvmField
    var paymentData: String
    @kotlin.jvm.JvmField
    var testCardNumber: String? = null

    init {
        this.firstName = firstName
        this.lastName = lastName
        this.retained = retained
        this.paymentData = paymentData
    }

    public override fun toJson(): JSONObject {
        val wrapper = JSONObject()
        val paymentMethod = JSONObject()
        val googlePay = JSONObject()
        addCommonJsonFields(paymentMethod, googlePay)
        googlePay.put("payment_data", paymentData)
        googlePay.put("test_card_number", testCardNumber)
        paymentMethod.put("google_pay", googlePay)
        wrapper.put("payment_method", paymentMethod)
        return wrapper
    }
}