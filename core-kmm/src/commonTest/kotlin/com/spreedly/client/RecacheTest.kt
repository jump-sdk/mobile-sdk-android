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
            firstName = "Joe",
            lastName = "Jones",
            number = client.createString("5555555555554444"),
            verificationValue = client.createString("432"),
            month = 3,
            year = 2032,
            retained = true,
        )
        val trans1 = client.createCreditCardPaymentMethod(cc)
        val trans2 = client.recache(trans1.result!!.token, client.createString("423"))
        assertNotNull(trans2.result?.token)
    }

    @Test
    fun BadTokenReturnsErrorMessage() = runTest {
        val trans = client.recache("notatoken", client.createString("423"))
        assertEquals("Unable to find the specified payment method.", trans.message)
    }
}
