package com.spreedly.client

import com.spreedly.client.models.CreditCardInfo
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class CreateCreditCardPaymentTest {
    lateinit var client: SpreedlyClient

    @BeforeTest
    fun initialize() {
        client = SpreedlyClient.newInstance(
            envKey = TestCredentials.user,
            envSecret = TestCredentials.password,
            test = true,
        )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun CreateCreditCardHasToken() = runTest {
        val cc = CreditCardInfo(
            firstName = "Joe",
            lastName = "Jones",
            number = client.createString("5555555555554444"),
            verificationValue = client.createString("432"),
            month = 2032,
            year = 12,
            retained = false,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertNotNull(trans.result?.token)
    }

    @Test
    fun RetainedCreateCreditCardHasToken() = runTest {
        val cc = CreditCardInfo(
            firstName = "Joe",
            lastName = "Jones",
            number = client.createString("5555555555554444"),
            verificationValue = client.createString("432"),
            month = 2032,
            year = 12,
            retained = true,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertNotNull(trans.result?.token)
    }

    @Test
    fun badCreditCardFails() = runTest {
        val cc = CreditCardInfo(
            firstName = "Joe",
            lastName = "Jones",
            number = client.createString("5555555555554444"),
            verificationValue = client.createString("432"),
            month = 2032,
            year = 0,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertEquals("Month can't be blank", trans.message)
    }

    @Test
    fun initializationFailsWithEmptyCredentials() = runTest {
        val badClient = SpreedlyClient.newInstance("", "", true)
        val cc = CreditCardInfo(
            firstName = "Joe",
            lastName = "Jones",
            number = client.createString("5555555555554444"),
            verificationValue = client.createString("432"),
            month = 2030,
            year = 12,
        )
        val trans = badClient.createCreditCardPaymentMethod(cc)
        assertEquals("You must specify an environment_key parameter.", trans.message)
    }
}
