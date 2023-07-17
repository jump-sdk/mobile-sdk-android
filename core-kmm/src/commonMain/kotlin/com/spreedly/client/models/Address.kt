package com.spreedly.client.models

import org.json.JSONObject
import java.io.Serializable

class Address : Serializable {
    @kotlin.jvm.JvmField
    var address1: String? = null
    @kotlin.jvm.JvmField
    var address2: String? = null
    @kotlin.jvm.JvmField
    var city: String? = null
    @kotlin.jvm.JvmField
    var state: String? = null
    @kotlin.jvm.JvmField
    var zip: String? = null
    @kotlin.jvm.JvmField
    var country: String? = null
    @kotlin.jvm.JvmField
    var phoneNumber: String? = null

    constructor() {}
    constructor(
        address1: String?,
        address2: String?,
        city: String?,
        state: String?,
        zip: String?,
        country: String?,
        phoneNumber: String?
    ) {
        this.address1 = address1
        this.address2 = address2
        this.city = city
        this.state = state
        this.zip = zip
        this.country = country
        this.phoneNumber = phoneNumber
    }

    fun toJson(json: JSONObject?, prefix: String): JSONObject {
        var json = json
        if (json == null) json = JSONObject()
        json.put(prefix + "address1", address1)
        json.put(prefix + "address2", address2)
        json.put(prefix + "city", city)
        json.put(prefix + "state", state)
        json.put(prefix + "zip", zip)
        json.put(prefix + "country", country)
        json.put(prefix + "phone_number", phoneNumber)
        return json
    }

    companion object {
        @kotlin.jvm.JvmStatic
        fun fromJson(json: JSONObject, prefix: String): Address? {
            return if (json.has(prefix + "address1")) {
                Address(
                    json.optString(prefix + "address1"),
                    json.optString(prefix + "address2"),
                    json.optString(prefix + "city"),
                    json.optString(prefix + "state"),
                    json.optString(prefix + "zip"),
                    json.optString(prefix + "country"),
                    json.optString(prefix + "phone_number")
                )
            } else {
                null
            }
        }
    }
}