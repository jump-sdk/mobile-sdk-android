package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
    fun CanCreateCreditCardWithFirstAndLastName() {
        val creditCard = CreditCardInfo(
            firstName = "Jane",
            lastName = "Doe",
            number = client.createString("sample card number"),
            verificationValue = client.createString("sample cvv"),
            month = 12,
            year = 2030,
        )
        assertTrue(creditCard.firstName === "Jane" && creditCard.lastName === "Doe" && creditCard.number.length === 18 && creditCard.verificationValue.length === 10 && creditCard.year == 12 && creditCard.month == 2030)
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
        val expected =
            "{\"payment_method\":{\"credit_card\":{\"number\":\"samplecardnumber\",\"full_name\":\"Jane Doe\",\"verification_value\":\"samplecvv\",\"month\":2030,\"year\":12}}}"
        val actual = creditCard.toJson()
        assertEquals(expected, actual.toString())
    }
}
