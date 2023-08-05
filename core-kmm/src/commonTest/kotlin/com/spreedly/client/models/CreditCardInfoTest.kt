package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertFailsWith

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
            number = client.createString("sample card number"),
            verificationValue = client.createString("sample cvv"),
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
            number = client.createString("sample card number"),
            verificationValue = client.createString("sample cvv"),
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
    fun CanNotEncodeCreditCardWithBothFullNameAndNameParts() {
        assertFailsWith<IllegalArgumentException> {
            val creditCard = CreditCardInfo(
                fullName = "Jane Doe",
                firstName = "Jane",
                lastName = "Doe",
                number = client.createString("sample card number"),
                verificationValue = client.createString("sample cvv"),
                month = 12,
                year = 2030,
            )
        }
    }
}
