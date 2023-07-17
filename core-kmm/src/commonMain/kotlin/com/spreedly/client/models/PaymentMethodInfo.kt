package com.spreedly.client.models

import org.json.JSONObject

abstract class PaymentMethodInfo {
    @kotlin.jvm.JvmField
    var company: String? = null
    @kotlin.jvm.JvmField
    var firstName: String? = null
    @kotlin.jvm.JvmField
    var lastName: String? = null
    @kotlin.jvm.JvmField
    var fullName: String? = null
    @kotlin.jvm.JvmField
    var address: Address? = null
    @kotlin.jvm.JvmField
    var shippingAddress: Address? = null
    @kotlin.jvm.JvmField
    var retained: Boolean? = null
    @kotlin.jvm.JvmField
    var metadata: JSONObject? = null
    @kotlin.jvm.JvmField
    var email: String? = null
    abstract fun toJson(): JSONObject

    protected constructor(copy: PaymentMethodInfo) {
        company = copy.company
        firstName = copy.firstName
        lastName = copy.lastName
        fullName = copy.fullName
        address = copy.address
        shippingAddress = copy.shippingAddress
        retained = copy.retained
        metadata = copy.metadata
        email = copy.email
    }

    protected constructor() {}

    fun addCommonJsonFields(paymentMethod: JSONObject, subType: JSONObject) {
        if (fullName != null) {
            subType.put("full_name", fullName)
        } else {
            subType.put("first_name", firstName)
            subType.put("last_name", lastName)
        }
        subType.put("company", company)
        if (address != null) {
            address!!.toJson(subType, "")
        }
        if (shippingAddress != null) {
            shippingAddress!!.toJson(subType, "shipping_")
        }
        paymentMethod.put("retained", retained)
        if (metadata != null) {
            paymentMethod.put("metadata", metadata)
        }
        if (email != null) {
            paymentMethod.put("email", email)
        }
    }
}