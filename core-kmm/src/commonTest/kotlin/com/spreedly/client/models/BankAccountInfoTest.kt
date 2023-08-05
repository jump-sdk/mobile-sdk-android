package com.spreedly.client.models

import com.spreedly.client.SpreedlyClient
import com.spreedly.client.TestCredentials
import com.spreedly.client.models.enums.AccountType
import kotlin.test.Test
import kotlin.test.assertContains

class BankAccountInfoTest {
    var client: SpreedlyClient = SpreedlyClient.newInstance(
        envKey = TestCredentials.user,
        envSecret = TestCredentials.password,
        test = true,
    )

    @Test
    fun CanEncodeBankAccount() {
        val bankAccount = BankAccountInfo(
            fullName = null,
            firstName = "Jane",
            lastName = "Doe",
            routingNumber = "1234567",
            accountNumber = client.createString("0000000"),
            accountType = AccountType.checking,
        )

        val json = bankAccount.toJson().toString()
        assertContains(json, "\"payment_method\":{\"bank_account\"")
        assertContains(json, "\"bank_account_number\":\"0000000\"")
        assertContains(json, "\"first_name\":\"Jane\"")
        assertContains(json, "\"last_name\":\"Doe\"")
        assertContains(json, "\"bank_routing_number\":\"1234567\"")
        assertContains(json, "\"bank_account_type\":\"checking\"")
    }

    @Test
    fun nullAccountTypeSetsEmptyString() {
        val bankAccount = BankAccountInfo(
            fullName = null,
            firstName = "Jane",
            lastName = "Doe",
            routingNumber = "1234567",
            accountNumber = client.createString("0000000"),
            accountType = null,
        )

        val json = bankAccount.toJson().toString()
        assertContains(json, "\"payment_method\":{\"bank_account\"")
        assertContains(json, "\"bank_account_number\":\"0000000\"")
        assertContains(json, "\"first_name\":\"Jane\"")
        assertContains(json, "\"last_name\":\"Doe\"")
        assertContains(json, "\"bank_routing_number\":\"1234567\"")
        assertContains(json, "\"bank_account_type\":\"\"")
    }
}
