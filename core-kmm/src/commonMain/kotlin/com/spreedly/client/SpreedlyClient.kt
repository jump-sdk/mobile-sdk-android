package com.spreedly.client

import com.spreedly.client.models.ApplePayInfo
import com.spreedly.client.models.BankAccountInfo
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.GooglePayInfo
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.results.BankAccountResult
import com.spreedly.client.models.results.CreditCardResult
import com.spreedly.client.models.results.TransactionResult

/**
 * SpreedlyClient
 *
 *
 * This class handles all the client side API communication.
 */
interface SpreedlyClient {
    /**
     * Creates a credit card payment method using the provided credit card information.
     *
     * @param info The credit card information used to create the payment method.
     * @return A [TransactionResult] containing the result of the credit card payment method creation.
     */
    suspend fun createCreditCardPaymentMethod(
        info: CreditCardInfo,
    ): TransactionResult<CreditCardResult>

    /**
     * Creates a bank payment method using the provided bank account information.
     *
     * @param info The bank account information used to create the payment method.
     * @return A [TransactionResult] containing the result of the bank payment method creation.
     */
    suspend fun createBankPaymentMethod(info: BankAccountInfo): TransactionResult<BankAccountResult>

    /**
     * Creates a Google Pay payment method using the provided Google Pay information.
     *
     * @param info The Google Pay information used to create the payment method.
     * @return A [TransactionResult] containing the result of the Google Pay payment method creation.
     */
    suspend fun createGooglePaymentMethod(info: GooglePayInfo): TransactionResult<CreditCardResult>

    /**
     * Creates an Apple Pay payment method using the provided Apple Pay information.
     *
     * @param info The Apple Pay information used to create the payment method.
     * @return A [TransactionResult] containing the result of the Apple Pay payment method creation.
     */
    suspend fun createApplePaymentMethod(info: ApplePayInfo): TransactionResult<CreditCardResult>

    /**
     * Recaches credit card information with the provided token and CVV.
     *
     * @param token The token associated with the credit card payment method.
     * @param cvv The secure opaque CVV to recache.
     * @return A [TransactionResult] containing the result of the recaching operation.
     */
    suspend fun recache(
        token: String,
        cvv: SpreedlySecureOpaqueString,
    ): TransactionResult<CreditCardResult>

    companion object {
        fun newInstance(envKey: String, test: Boolean): SpreedlyClient {
            return SpreedlyClientImpl(envKey, null, test)
        }

        /**
         * Creates a new instance of the Spreedly client with the provided environment key and secret.
         *
         * @param envKey The environment key for accessing the Spreedly services.
         * @param envSecret The environment secret for accessing the Spreedly services.
         * @param test Indicates whether the client is meant for testing purposes.
         * @return An instance of the Spreedly client.
         */
        fun newInstance(envKey: String, envSecret: String, test: Boolean): SpreedlyClient {
            return SpreedlyClientImpl(envKey, envSecret, test)
        }
    }
}
