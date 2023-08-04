package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import com.spreedly.client.models.enums.AccountType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BankAccountInfoTest {
    var client: SpreedlyClient = SpreedlyClient.newInstance(
        envKey = TestCredentials.user,
        envSecret = TestCredentials.password,
        test = true,
    )

    @Test
    fun CanCreateBankAccountWithFirstAndLast() {
        val bankAccount = BankAccountInfo(
            "Jane",
            "Doe",
            "1234567",
            client.createString("0000000"),
            AccountType.checking,
        )
        assertTrue(bankAccount.firstName === "Jane" && bankAccount.lastName === "Doe" && bankAccount.routingNumber === "1234567" && bankAccount.accountNumber.length === 7 && bankAccount.accountType === AccountType.checking)
    }

    @Test
    fun CanEncodeBankAccount() {
        val bankAccount = BankAccountInfo(
            firstName = "Jane",
            lastName = "Doe",
            routingNumber = "1234567",
            accountNumber = client.createString("0000000"),
            accountType = AccountType.checking,
        )
        val expected =
            "{\"payment_method\":{\"bank_account\":{\"bank_account_number\":\"0000000\",\"full_name\":\"Jane Doe\",\"bank_routing_number\":\"1234567\",\"bank_account_type\":\"checking\"}}}"
        val actual = bankAccount.toJson()
        assertEquals(expected, actual.toString())
    }

    @Test
    fun nullAccountTypeSetsEmptyString() {
        val bankAccount =
            BankAccountInfo(
                firstName = "Jane",
                lastName = "Doe",
                routingNumber = "1234567",
                accountNumber = client.createString("0000000"),
                accountType = null,
            )
        val expected =
            "{\"payment_method\":{\"bank_account\":{\"bank_account_number\":\"0000000\",\"full_name\":\"Jane Doe\",\"bank_routing_number\":\"1234567\",\"bank_account_type\":\"\"}}}"
        val actual = bankAccount.toJson()
        assertEquals(expected, actual.toString())
    }
}
