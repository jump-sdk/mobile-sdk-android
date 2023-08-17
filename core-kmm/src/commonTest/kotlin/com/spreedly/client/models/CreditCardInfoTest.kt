package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonPrimitive

class CreditCardInfoTest {

    lateinit var client: SpreedlyClient

    @BeforeTest
    fun initialize() {
        client = SpreedlyClient.newInstance(
            envKey = TestCredentials.user,
            envSecret = TestCredentials.password,
            test = true,
        )
    }

    @Test
    fun CanEncodeCreditCard() {
        val creditCard = CreditCardInfo(
            fullName = null,
            firstName = "Jane",
            lastName = "Doe",
            number = SpreedlySecureOpaqueString("sample card number"),
            verificationValue = SpreedlySecureOpaqueString("sample cvv"),
            month = 12,
            year = 2030,
        )

        val json = creditCard.toJson().toString()
        assertContains(json, "\"payment_method\":{\"credit_card\"")
        assertContains(json, "\"number\":\"samplecardnumber\"")
        assertContains(json, "\"first_name\":\"Jane\"")
        assertContains(json, "\"last_name\":\"Doe\"")
        assertContains(json, "\"verification_value\":\"samplecvv\"")
        assertContains(json, "\"month\":12")
        assertContains(json, "\"year\":2030")
    }

    @Test
    fun CanEncodeCreditCardWithFullName() {
        val creditCard = CreditCardInfo(
            fullName = "Jane Doe",
            firstName = null,
            lastName = null,
            number = SpreedlySecureOpaqueString("sample card number"),
            verificationValue = SpreedlySecureOpaqueString("sample cvv"),
            month = 12,
            year = 2030,
        )

        val json = creditCard.toJson().toString()
        assertContains(json, "\"payment_method\":{\"credit_card\"")
        assertContains(json, "\"number\":\"samplecardnumber\"")
        assertContains(json, "\"full_name\":\"Jane Doe\"")
        assertContains(json, "\"verification_value\":\"samplecvv\"")
        assertContains(json, "\"month\":12")
        assertContains(json, "\"year\":2030")
    }

    @Test
    fun CanEncodeRetainedCreditCardWithFullNameInPaymentMethod() {
        val creditCardInfo = CreditCardInfo(
            fullName = "Jane Doe",
            firstName = null,
            lastName = null,
            number = SpreedlySecureOpaqueString("sample card number"),
            verificationValue = SpreedlySecureOpaqueString("sample cvv"),
            month = 12,
            year = 2030,
            retained = true,
        )

        val json = creditCardInfo.toJson()
        val paymentMethod = json["payment_method"] as JsonObject
        val creditCard = paymentMethod["credit_card"] as JsonObject
        assertEquals(
            expected = "Jane Doe",
            actual = creditCard["full_name"]!!.jsonPrimitive.content,
        )
        assertTrue(paymentMethod["retained"]!!.jsonPrimitive.boolean)
    }
}
