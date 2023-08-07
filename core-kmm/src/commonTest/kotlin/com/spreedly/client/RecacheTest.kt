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
class RecacheTest {
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
    fun RecacheReturnsToken() = runTest {
        val cc = CreditCardInfo(
            fullName = null,
            firstName = "Joe",
            lastName = "Jones",
            number = SpreedlySecureOpaqueString("5555555555554444"),
            verificationValue = SpreedlySecureOpaqueString("432"),
            month = 3,
            year = 2032,
            retained = true,
        )
        val trans1 = client.createCreditCardPaymentMethod(cc)
        val trans2 = client.recache(trans1.result!!.token!!, SpreedlySecureOpaqueString("423"))
        assertNotNull(trans2.result?.token)
    }

    @Test
    fun BadTokenReturnsErrorMessage() = runTest {
        val trans = client.recache("notatoken", SpreedlySecureOpaqueString("423"))
        assertEquals(
            expected = "Unable to find the specified payment method.",
            actual = trans.errors?.first()?.message,
        )
    }
}
