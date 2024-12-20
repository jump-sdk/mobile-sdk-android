package com.spreedly.client

import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.SpreedlySecureOpaqueString
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
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
            fullName = null,
            firstName = "Joe",
            lastName = "Jones",
            number = SpreedlySecureOpaqueString("5555555555554444"),
            verificationValue = SpreedlySecureOpaqueString("432"),
            month = 12,
            year = 2032,
            retained = false,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertNotNull(trans.result?.token)
    }

    @Test
    fun RetainedCreateCreditCardHasToken() = runTest {
        val cc = CreditCardInfo(
            fullName = null,
            firstName = "Joe",
            lastName = "Jones",
            number = SpreedlySecureOpaqueString("5555555555554444"),
            verificationValue = SpreedlySecureOpaqueString("432"),
            month = 12,
            year = 2032,
            retained = true,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertNotNull(trans.result?.token)
        assertEquals("4444", trans.result?.lastFourDigits)
        assertEquals("555555", trans.result?.firstSixDigits)
    }

    @Test
    fun badCreditCardYearFails() = runTest {
        val cc = CreditCardInfo(
            fullName = null,
            firstName = "Joe",
            lastName = "Jones",
            number = SpreedlySecureOpaqueString("4111111111111111"),
            verificationValue = SpreedlySecureOpaqueString("432"),
            month = 12,
            year = 3000,
        )
        val trans = client.createCreditCardPaymentMethod(cc)
        assertEquals(expected = "year", actual = trans.errors?.first()?.attribute)
    }

    @Test
    fun initializationFailsWithEmptyCredentials() = runTest {
        val badClient = SpreedlyClient.newInstance("", "", true)
        val cc = CreditCardInfo(
            fullName = null,
            firstName = "Joe",
            lastName = "Jones",
            number = SpreedlySecureOpaqueString("5555555555554444"),
            verificationValue = SpreedlySecureOpaqueString("432"),
            month = 12,
            year = 2030,
        )
        val trans = badClient.createCreditCardPaymentMethod(cc)
        assertEquals(
            expected = "You must specify an environment_key parameter.",
            actual = trans.errors?.first()?.message,
        )
    }
}
