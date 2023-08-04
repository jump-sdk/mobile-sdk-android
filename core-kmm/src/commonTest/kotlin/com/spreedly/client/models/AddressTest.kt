package com.spreedly.client.models

import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

class AddressTest {
    lateinit var jsonAddress: JsonObject
    lateinit var jsonAddressWithPrefix: JsonObject
    var address =
        Address("555 Sample Rd", "Unit 2", "Anytown", "WA", "55555", "USA", "444-444-4444")

    @BeforeTest
    fun initialize() {
        val address = mutableMapOf<String, String>()
        val addressWithPrefix = mutableMapOf<String, String>()
        address.put("address1", "555 Sample Rd")
        address.put("address2", "Unit 2")
        address.put("city", "Anytown")
        address.put("state", "WA")
        address.put("zip", "55555")
        address.put("country", "USA")
        address.put("phone_number", "444-444-4444")
        jsonAddress = JsonObject(address.map { (k, v) -> k to JsonPrimitive(v) }.toMap())
        addressWithPrefix.put("shipping_address_address1", "555 Sample Rd")
        addressWithPrefix.put("shipping_address_address2", "Unit 2")
        addressWithPrefix.put("shipping_address_city", "Anytown")
        addressWithPrefix.put("shipping_address_state", "WA")
        addressWithPrefix.put("shipping_address_zip", "55555")
        addressWithPrefix.put("shipping_address_country", "USA")
        addressWithPrefix.put("shipping_address_phone_number", "444-444-4444")
        jsonAddressWithPrefix = JsonObject(addressWithPrefix.map { (k, v) -> k to JsonPrimitive(v) }.toMap())
    }

    @Test
    fun fromJsonJsonObjectNonNull() {
        val actualAddress: Address? = Address.fromJson(jsonAddress, "")
        assertEquals(address.address1, actualAddress!!.address1)
        assertEquals(address.address2, actualAddress!!.address2)
        assertEquals(address.city, actualAddress!!.city)
        assertEquals(address.state, actualAddress!!.state)
        assertEquals(address.zip, actualAddress!!.zip)
        assertEquals(address.phoneNumber, actualAddress!!.phoneNumber)
        assertEquals(address.country, actualAddress!!.country)
    }

    @Test
    fun toJsonJsonObjectNonNull() {
        val actualJson = address.toJson(JsonObject(mapOf()), "")
        assertEquals(actualJson.toString(), jsonAddress.toString())
    }

    @Test
    fun fromJsonJsonObjectNull() {
        var actualAddress: Address? = null
        actualAddress = Address.fromJson(jsonAddressWithPrefix, "shipping_address_")
        assertEquals(address.address1, actualAddress!!.address1)
        assertEquals(address.address2, actualAddress!!.address2)
        assertEquals(address.city, actualAddress!!.city)
        assertEquals(address.state, actualAddress!!.state)
        assertEquals(address.zip, actualAddress!!.zip)
        assertEquals(address.phoneNumber, actualAddress!!.phoneNumber)
        assertEquals(address.country, actualAddress!!.country)
    }

    @Test
    fun toJsonJsonObjectNull() {
        val actualJson = address.toJson(null, "")
        assertEquals(actualJson.toString(), jsonAddress.toString())
    }

    @Test
    fun fromJsonReturnsNullWithBadPrefix() {
        var actualAddress: Address? = null
        actualAddress = Address.fromJson(jsonAddress, "bad_prefix")
        assertNull(actualAddress)
    }

    @Test
    fun canCreateEmptyAddress() {
        val address = Address()
        assertNotNull(address)
    }
}
