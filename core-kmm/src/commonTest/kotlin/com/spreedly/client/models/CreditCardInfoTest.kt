package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains

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
}
