package com.spreedly.client

import com.spreedly.client.models.BankAccountInfo
import com.spreedly.client.models.enums.AccountType
import com.spreedly.client.models.results.PaymentMethodResult
import com.spreedly.client.models.results.TransactionResult
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

class CreateBankAccountPaymentTest {
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
    fun CreateBankAccountGetsToken() = runTest {
        val bankAccountInfo = BankAccountInfo(
            firstName = "John",
            lastName = "Doe",
            routingNumber = "021000021",
            accountNumber = client.createString("9876543210"),
            accountType = AccountType.checking,
        )
        val trans = client.createBankPaymentMethod(bankAccountInfo)
        assertNotNull(trans.result?.token)
    }

    @Test
    fun BadInfoReturnsErrors() = runTest {
        val bankAccountInfo = BankAccountInfo(
            firstName = "",
            lastName = "",
            routingNumber = "021000021",
            accountNumber = client.createString("9876543210"),
            accountType = AccountType.checking,
            retained = false,
        )
        val trans = client.createBankPaymentMethod(bankAccountInfo)
        assertEquals("Full name can't be blank", trans.errors?.get(0)?.message)
    }

    @Test
    fun RetainedCreateBankAccountGetsToken() = runTest {
        val bankAccountInfo = BankAccountInfo(
            firstName = "John",
            lastName = "Doe",
            routingNumber = "021000021",
            accountNumber = client.createString("9876543210"),
            accountType = AccountType.checking,
            retained = true,
        )
        val trans = client.createBankPaymentMethod(bankAccountInfo)
        assertNotNull(trans.result?.token)
    }
}
