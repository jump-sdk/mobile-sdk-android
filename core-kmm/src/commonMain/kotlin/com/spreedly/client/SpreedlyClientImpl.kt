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
import com.spreedly.client.models.results.PaymentMethodResult
import com.spreedly.client.models.results.SpreedlyError
import com.spreedly.client.models.results.TransactionResult
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter
import io.reactivex.rxjava3.functions.Function
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request.Builder.build
import okhttp3.Request.Builder.header
import okhttp3.Request.Builder.method
import okhttp3.Request.Builder.url
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.Serializable
import java.nio.charset.StandardCharsets
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.Properties

internal class SpreedlyClientImpl(
    private val key: String,
    private val secret: String?,
    private val test: Boolean
) : SpreedlyClient, Serializable {
    private var platformData: String? = null
    private val authenticatedURL = "/payment_methods.json"
    private val unauthenticatedURL = "/payment_methods/restricted.json"
    override fun createString(string: String): SpreedlySecureOpaqueString {
        val spreedlySecureOpaqueString = SpreedlySecureOpaqueString()
        spreedlySecureOpaqueString.append(string)
        return spreedlySecureOpaqueString
    }

    override fun createCreditCardPaymentMethod(info: CreditCardInfo): Single<TransactionResult<CreditCardResult>> {
        val authenticated = shouldDoAuthenticatedRequest(info)
        val url = if (authenticated) authenticatedURL else unauthenticatedURL
        return sendRequest(info.toJson(), url, authenticated).map(
            Function<JSONObject, TransactionResult<CreditCardResult>> { raw: JSONObject ->
                processCCMap(
                    raw
                )
            })
    }

    private fun shouldDoAuthenticatedRequest(info: PaymentMethodInfo): Boolean {
        return info.retained != null && info.retained!! && secret != null
    }

    override fun createBankPaymentMethod(info: BankAccountInfo): Single<TransactionResult<BankAccountResult?>> {
        val authenticated = shouldDoAuthenticatedRequest(info)
        val url = if (authenticated) authenticatedURL else unauthenticatedURL
        return sendRequest(info.toJson(), url, authenticated).map { raw: JSONObject ->
            processBAMap(
                raw
            )
        }
    }

    override fun createGooglePaymentMethod(info: GooglePayInfo): Single<TransactionResult<PaymentMethodResult>> {
        val authenticated = shouldDoAuthenticatedRequest(info)
        val url = if (authenticated) authenticatedURL else unauthenticatedURL
        return sendRequest(info.toJson(), url, authenticated).map(
            Function<JSONObject, TransactionResult<PaymentMethodResult>> { raw: JSONObject ->
                processCCMap(
                    raw
                )
            })
    }

    override fun createApplePaymentMethod(info: ApplePayInfo): Single<TransactionResult<PaymentMethodResult>> {
        val authenticated = shouldDoAuthenticatedRequest(info)
        val url = if (authenticated) authenticatedURL else unauthenticatedURL
        return sendRequest(info.toJson(), url, authenticated).map(
            Function<JSONObject, TransactionResult<PaymentMethodResult>> { raw: JSONObject ->
                processCCMap(
                    raw
                )
            })
    }

    override fun recache(
        token: String,
        cvv: SpreedlySecureOpaqueString
    ): Single<TransactionResult<PaymentMethodResult>> {
        return sendRequest(
            RecacheInfo(cvv).toJson(),
            "/payment_methods/$token/recache.json",
            true
        ).map(
            Function<JSONObject, TransactionResult<PaymentMethodResult>> { raw: JSONObject ->
                processCCMap(
                    raw
                )
            })
    }

    fun <T : PaymentMethodResult?> processCCMap(raw: JSONObject): TransactionResult<T?> {
        val transactionResult: TransactionResult<T?>
        var rawTransaction = raw.optJSONObject("transaction")
        if (rawTransaction == null) {
            rawTransaction = JSONObject()
        }
        val rawResult = rawTransaction.optJSONObject("payment_method")
        var result: CreditCardResult? = null
        if (rawResult != null) {
            result = CreditCardResult(
                rawResult.optString("token"),
                rawResult.optString("storage_state"),
                rawResult.optBoolean("test", test),
                rawResult.optString("payment_method_type"),
                processErrors(rawResult.optJSONArray("errors")),
                parseDate(rawTransaction.optString("created_at")),
                parseDate(rawTransaction.optString("updated_at")),
                rawResult.optString("email"),
                rawResult.optString("last_four_digits"),
                rawResult.optString("first_six_digits"),
                rawResult.optString("verification_value"),
                rawResult.optString("card_type"),
                rawResult.optString("number"),
                rawResult.optString("month"),
                rawResult.optString("year")
            )
        }
        transactionResult = TransactionResult(
            rawTransaction.optString("token"),
            parseDate(rawTransaction.optString("created_at")),
            parseDate(rawTransaction.optString("updated_at")),
            rawTransaction.optBoolean("succeeded", false),
            rawTransaction.optString("transaction_type"),
            rawTransaction.optBoolean("retained", false),
            rawTransaction.optString("state"),
            rawTransaction.optString("messageKey"),
            rawTransaction.optString("message"),
            processErrors(raw.optJSONArray("errors")),
            result as T?
        )
        return transactionResult
    }

    fun processBAMap(raw: JSONObject): TransactionResult<BankAccountResult?> {
        val transactionResult: TransactionResult<BankAccountResult?>
        var rawTransaction = raw.optJSONObject("transaction")
        if (rawTransaction == null) {
            rawTransaction = JSONObject()
        }
        val rawResult = rawTransaction.optJSONObject("payment_method")
        var result: BankAccountResult? = null
        if (rawResult != null) {
            result = BankAccountResult(
                rawResult.optString("token"),
                rawResult.optString("storage_state"),
                rawResult.optBoolean("test", true),
                rawResult.optString("payment_method_type"),
                parseDate(rawTransaction.optString("created_at")),
                parseDate(rawTransaction.optString("updated_at")),
                rawResult.optString("email"),
                processErrors(rawResult.optJSONArray("errors")),
                rawResult.optString("bank_name"),
                rawResult.optString("account_type"),
                rawResult.optString("account_holder_type"),
                rawResult.optString("routing_number_display_digits"),
                rawResult.optString("account_number_display_digits"),
                rawResult.optString("routing_number"),
                rawResult.optString("account_number"),
                rawResult.optString("first_name"),
                rawResult.optString("last_name"),
                rawResult.optString("full_name")
            )
        }
        transactionResult = TransactionResult(
            rawTransaction.optString("token"),
            parseDate(rawTransaction.optString("created_at")),
            parseDate(rawTransaction.optString("updated_at")),
            rawTransaction.optBoolean("succeeded", false),
            rawTransaction.optString("transaction_type"),
            rawTransaction.optBoolean("retained", false),
            rawTransaction.optString("state"),
            rawTransaction.optString("messageKey"),
            rawTransaction.optString("message"),
            processErrors(raw.optJSONArray("errors")),
            result
        )
        return transactionResult
    }

    private fun sendRequest(
        requestBody: JSONObject,
        url: String,
        authenticated: Boolean
    ): Single<JSONObject> {
        val baseUrl = "https://core.spreedly.com/v1"
        val builder: Builder = Builder().url(baseUrl + url)
        if (!authenticated) {
            requestBody.put("environment_key", key)
        } else {
            builder.header("Authorization", credentials)
        }
        requestBody.put("platform-meta", getPlatformData())
        val call = OkHttpClient().newCall(
            builder
                .method(
                    "POST",
                    RequestBody.create(requestBody.toString(), parse.parse("application/json"))
                )
                .build()
        )
        return Single.create { emitter: SingleEmitter<JSONObject> ->
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    emitter.onError(e)
                }

                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        emitter.onSuccess(
                            JSONObject(
                                Objects.requireNonNull(response.body).string()
                            )
                        )
                    } catch (npe: NullPointerException) {
                        emitter.onError(npe)
                    }
                }
            })
        }
    }

    private fun processErrors(errors: JSONArray?): List<SpreedlyError>? {
        if (errors == null) {
            return null
        }
        val spreedlyErrors = ArrayList<SpreedlyError>()
        for (i in 0 until errors.length()) {
            val json = errors.getJSONObject(i)
            spreedlyErrors.add(
                SpreedlyError(
                    json.optString("attribute"),
                    json.optString("key"),
                    json.optString("message")
                )
            )
        }
        return spreedlyErrors
    }

    fun parseDate(dateString: String?): Date? {
        var dateString = dateString ?: return null
        dateString = dateString.replace('T', ' ')
        dateString = dateString.replace("Z", "+0000")
        var date: Date? = null
        date = try {
            SimpleDateFormat("yyyy-MM-dd hh:mm:ssZ", Locale.US).parse(dateString)
        } catch (e: ParseException) {
            return null
        }
        return date
    }

    override val credentials: String?
        get() {
            val raw = "$key:$secret"
            return "Basic " + safeBase64(raw.toByteArray())
        }
    override val platformLocalData: String
        get() = if (platformData != null) platformData else getPlatformData()
            .also { platformData = it }

    override fun setPlatformData(data: String?) {
        platformData = data
    }

    companion object {
        private fun safeBase64(source: ByteArray): String {
            return try {
                val dtc = Class.forName("android.util.Base64")
                dtc.getMethod("encodeToString", ByteArray::class.java, Int::class.javaPrimitiveType)
                    .invoke(null, source, 2) as String
            } catch (e: ReflectiveOperationException) {
                try {
                    val dtc = Class.forName("java.util.Base64")
                    val encoder = dtc.getMethod("getEncoder").invoke(null)
                    val r =
                        encoder.javaClass.getMethod("encodeToString", ByteArray::class.java).invoke(
                            encoder, *arrayOf<Any>(
                                source
                            )
                        )
                    if (r is String) r else {
                        val bytes = r as ByteArray
                        String(bytes, 0, 0, bytes.size)
                    }
                } catch (e2: ReflectiveOperationException) {
                    println(e2)
                    ""
                }
            }
        }

        fun getPlatformData(): String {
            val data = JSONObject()
            data.put("core-version", BuildInfo.VERSION)
            data.put("platform", "java")
            val properties = System.getProperties()
            setFromProperty(data, properties, "locale", "user.locale")
            val os = JSONObject()
            setFromProperty(os, properties, "name", "os.name")
            setFromProperty(os, properties, "arch", "os.arch")
            setFromProperty(os, properties, "version", "os.version")
            data.put("os", os)
            val git = JSONObject()
            git.put("branch", BuildInfo.GIT_BRANCH)
            git.put("tag", BuildInfo.GIT_TAG)
            git.put("commit", BuildInfo.GIT_COMMIT)
            data.put("git", git)
            try {
                val Build = Class.forName("android.os.Build")
                val device = JSONObject()
                device.put("model", Build.getField("MODEL")[null])
                device.put("manufacturer", Build.getField("MANUFACTURER")[null])
                device.put("device", Build.getField("DEVICE")[null])
                device.put("brand", Build.getField("BRAND")[null])
                data.put("device", device)
            } catch (e: Exception) {
            }
            val bytes = StandardCharsets.UTF_8.encode(data.toString()).array()
            return safeBase64(bytes)
        }

        fun setFromProperty(
            data: JSONObject,
            properties: Properties,
            jskey: String?,
            propkey: String?
        ) {
            try {
                data.put(jskey, properties.getProperty(propkey, "unknown"))
            } catch (e: Exception) {
            }
        }
    }
}