package com.spreedly.client

import com.spreedly.client.models.ApplePayInfo
import com.spreedly.client.models.BankAccountInfo
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.GooglePayInfo
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.results.BankAccountResult
import com.spreedly.client.models.results.CreditCardResult
import com.spreedly.client.models.results.PaymentMethodResult
import com.spreedly.client.models.results.TransactionResult

/**
 * SpreedlyClient
 *
 *
 * This class handles all the client side API communication.
 */
interface SpreedlyClient {
    fun createString(string: String): SpreedlySecureOpaqueString
    suspend fun createCreditCardPaymentMethod(info: CreditCardInfo): TransactionResult<CreditCardResult>
    suspend fun createBankPaymentMethod(info: BankAccountInfo): TransactionResult<BankAccountResult?>
    suspend fun createGooglePaymentMethod(info: GooglePayInfo): TransactionResult<CreditCardResult>
    suspend fun createApplePaymentMethod(info: ApplePayInfo): TransactionResult<CreditCardResult>
    suspend fun recache(
        token: String,
        cvv: SpreedlySecureOpaqueString,
    ): TransactionResult<CreditCardResult>

    val platformLocalData: String

    companion object {
        fun newInstance(envKey: String, test: Boolean): SpreedlyClient {
            return SpreedlyClientImpl(envKey, null, test)
        }

        @kotlin.jvm.JvmStatic
        fun newInstance(envKey: String, envSecret: String, test: Boolean): SpreedlyClient {
            return SpreedlyClientImpl(envKey, envSecret, test)
        }

        @kotlin.jvm.JvmStatic
        val platformData: String
            get() = SpreedlyClientImpl.Companion.getPlatformData()
    }
}