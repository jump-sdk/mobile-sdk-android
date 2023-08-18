package com.spreedly.client

import com.spreedly.client.models.ApplePayInfo
import com.spreedly.client.models.BankAccountInfo
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.GooglePayInfo
import com.spreedly.client.models.PaymentMethodInfo
import com.spreedly.client.models.RecacheInfo
import com.spreedly.client.models.SpreedlySecureOpaqueString
import com.spreedly.client.models.results.BankAccountResult
import com.spreedly.client.models.results.CreditCardResult
import com.spreedly.client.models.results.SpreedlyError
import com.spreedly.client.models.results.StorageState
import com.spreedly.client.models.results.TransactionResult
import kotlinx.datetime.Instant
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

// https://docs.spreedly.com/reference/api/v1/
internal class SpreedlyClientImpl(
    key: String,
    private val secret: String?,
    private val test: Boolean,
) : SpreedlyClient {
    private val client = KtorClient(key = key, secret = secret)
    private val authenticatedURL = "/v1/payment_methods.json"
    private val unauthenticatedURL = "/v1/payment_methods/restricted.json"

    override suspend fun createCreditCardPaymentMethod(
        info: CreditCardInfo,
    ): TransactionResult<CreditCardResult> {
        val (authenticated, url) = isAuthenticatedUrl(info)
        return client.sendRequest(info.toJson(), url, authenticated)
            .let { processCCMap(it) }
    }

    private fun isAuthenticatedUrl(info: PaymentMethodInfo): Pair<Boolean, String> {
        val authenticated = info.retained == true && secret != null
        return authenticated to if (authenticated) authenticatedURL else unauthenticatedURL
    }

    override suspend fun createBankPaymentMethod(
        info: BankAccountInfo,
    ): TransactionResult<BankAccountResult> {
        val (authenticated, url) = isAuthenticatedUrl(info)
        return client.sendRequest(info.toJson(), url, authenticated)
            .let { processBAMap(it) }
    }

    override suspend fun createGooglePaymentMethod(
        info: GooglePayInfo,
    ): TransactionResult<CreditCardResult> {
        val (authenticated, url) = isAuthenticatedUrl(info)
        return client.sendRequest(info.toJson(), url, authenticated)
            .let { processCCMap(it) }
    }

    override suspend fun createApplePaymentMethod(
        info: ApplePayInfo,
    ): TransactionResult<CreditCardResult> {
        val (authenticated, url) = isAuthenticatedUrl(info)
        return client.sendRequest(info.toJson(), url, authenticated)
            .let { processCCMap(it) }
    }

    override suspend fun recache(
        token: String,
        cvv: SpreedlySecureOpaqueString,
    ): TransactionResult<CreditCardResult> {
        return client.sendRequest(
            requestBody = RecacheInfo(cvv).toJson(),
            url = "/v1/payment_methods/$token/recache.json",
            authenticated = true,
        )
            .let { processCCMap(it) }
    }

    companion object {
        private fun processCCMap(raw: JsonObject): TransactionResult<CreditCardResult> {
            val rawTransaction = raw["transaction"] as? JsonObject ?: JsonObject(emptyMap())
            val rawResult = rawTransaction["payment_method"] as? JsonObject
            val result: CreditCardResult? = rawResult?.let {
                CreditCardResult(
                    token = rawResult["token"]?.jsonPrimitive?.content,
                    storageState = rawResult["storage_state"]?.jsonPrimitive?.contentOrNull,
                    test = rawResult["test"]?.jsonPrimitive?.booleanOrNull,
                    paymentMethodType = rawResult["payment_method_type"]?.jsonPrimitive?.contentOrNull,
                    errors = processErrors(rawResult["errors"] as? JsonArray),
                    createdAt = parseDate(
                        rawTransaction["created_at"]?.jsonPrimitive?.contentOrNull,
                    ),
                    updatedAt = parseDate(
                        rawTransaction["updated_at"]?.jsonPrimitive?.contentOrNull,
                    ),
                    email = rawResult["email"]?.jsonPrimitive?.contentOrNull,
                    lastFourDigits = rawResult["last_four_digits"]?.jsonPrimitive?.contentOrNull,
                    firstSixDigits = rawResult["first_six_digits"]?.jsonPrimitive?.contentOrNull,
                    cvv = rawResult["verification_value"]?.jsonPrimitive?.contentOrNull,
                    cardType = rawResult["card_type"]?.jsonPrimitive?.contentOrNull,
                    number = rawResult["number"]?.jsonPrimitive?.contentOrNull,
                    month = rawResult["month"]?.jsonPrimitive?.contentOrNull,
                    year = rawResult["year"]?.jsonPrimitive?.contentOrNull,
                )
            }
            return TransactionResult(
                transactionToken = rawTransaction["token"]?.jsonPrimitive?.contentOrNull,
                createdAt = parseDate(rawTransaction["created_at"]?.jsonPrimitive?.contentOrNull),
                updatedAt = parseDate(rawTransaction["updated_at"]?.jsonPrimitive?.contentOrNull),
                succeeded = rawTransaction["succeeded"]?.jsonPrimitive?.booleanOrNull ?: false,
                transactionType = rawTransaction["transaction_type"]?.jsonPrimitive?.contentOrNull,
                retained = rawTransaction["retained"]?.jsonPrimitive?.booleanOrNull ?: false,
                storageState = rawTransaction["state"]?.jsonPrimitive?.contentOrNull?.let {
                    StorageState.fromString(it)
                },
                state = rawTransaction["state"]?.jsonPrimitive?.contentOrNull,
                messageKey = rawTransaction["messageKey"]?.jsonPrimitive?.contentOrNull,
                message = rawTransaction["message"]?.jsonPrimitive?.contentOrNull,
                errors = processErrors(raw["errors"] as? JsonArray),
                result = result,
            )
        }

        private fun processBAMap(raw: JsonObject): TransactionResult<BankAccountResult> {
            val rawTransaction = raw["transaction"] as? JsonObject ?: JsonObject(emptyMap())
            val rawResult = rawTransaction["payment_method"] as? JsonObject
            val result: BankAccountResult? = rawResult?.let {
                BankAccountResult(
                    token = rawResult["token"]?.jsonPrimitive?.contentOrNull,
                    storageState = rawResult["storage_state"]?.jsonPrimitive?.contentOrNull,
                    test = rawResult["test"]?.jsonPrimitive?.booleanOrNull,
                    paymentMethodType = rawResult["payment_method_type"]?.jsonPrimitive?.contentOrNull,
                    createdAt = parseDate(
                        rawTransaction["created_at"]?.jsonPrimitive?.contentOrNull,
                    ),
                    updatedAt = parseDate(
                        rawTransaction["updated_at"]?.jsonPrimitive?.contentOrNull,
                    ),
                    email = rawResult["email"]?.jsonPrimitive?.contentOrNull,
                    errors = processErrors(rawResult["errors"] as? JsonArray),
                    bankName = rawResult["bank_name"]?.jsonPrimitive?.contentOrNull,
                    accountType = rawResult["account_type"]?.jsonPrimitive?.contentOrNull,
                    accountHolderType = rawResult["account_holder_type"]?.jsonPrimitive?.contentOrNull,
                    routingNumberDisplayDigits = rawResult["routing_number_display_digits"]?.jsonPrimitive?.contentOrNull,
                    accountNumberDisplayDigits = rawResult["account_number_display_digits"]?.jsonPrimitive?.contentOrNull,
                    routingNumber = rawResult["routing_number"]?.jsonPrimitive?.contentOrNull,
                    accountNumber = rawResult["account_number"]?.jsonPrimitive?.contentOrNull,
                    firstName = rawResult["first_name"]?.jsonPrimitive?.contentOrNull,
                    lastName = rawResult["last_name"]?.jsonPrimitive?.contentOrNull,
                    fullName = rawResult["full_name"]?.jsonPrimitive?.contentOrNull,
                )
            }
            return TransactionResult(
                transactionToken = rawTransaction["token"]?.jsonPrimitive?.contentOrNull,
                createdAt = parseDate(rawTransaction["created_at"]?.jsonPrimitive?.contentOrNull),
                updatedAt = parseDate(rawTransaction["updated_at"]?.jsonPrimitive?.contentOrNull),
                succeeded = rawTransaction["succeeded"]?.jsonPrimitive?.booleanOrNull ?: false,
                transactionType = rawTransaction["transaction_type"]?.jsonPrimitive?.contentOrNull,
                retained = rawTransaction["retained"]?.jsonPrimitive?.booleanOrNull ?: false,
                storageState = rawTransaction["state"]?.jsonPrimitive?.contentOrNull?.let {
                    StorageState.fromString(it)
                },
                state = rawTransaction["state"]?.jsonPrimitive?.contentOrNull,
                messageKey = rawTransaction["messageKey"]?.jsonPrimitive?.contentOrNull,
                message = rawTransaction["message"]?.jsonPrimitive?.contentOrNull,
                errors = processErrors(raw["errors"] as? JsonArray),
                result = result,
            )
        }

        private fun processErrors(errors: JsonArray?): List<SpreedlyError>? {
            return errors
                ?.mapNotNull { it as? JsonObject }
                ?.map {
                    SpreedlyError(
                        it["attribute"]?.jsonPrimitive?.contentOrNull,
                        it["key"]?.jsonPrimitive?.contentOrNull,
                        it["message"]?.jsonPrimitive?.contentOrNull,
                    )
                }
        }

        fun parseDate(dateString: String?): Instant? {
            if (dateString == null) {
                return null
            }
            return try {
                Instant.parse(dateString)
            } catch (e: Exception) {
                null
            }
        }
    }
}
