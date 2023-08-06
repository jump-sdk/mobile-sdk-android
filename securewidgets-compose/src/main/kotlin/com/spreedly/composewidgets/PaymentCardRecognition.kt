package com.spreedly.composewidgets

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.wallet.PaymentCardRecognitionIntentRequest
import com.google.android.gms.wallet.PaymentCardRecognitionResult
import com.google.android.gms.wallet.Wallet
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object PaymentCardRecognition {
    suspend fun getIntent(
        context: Context,
        environment: Int, // WalletConstants.ENVIRONMENT_PRODUCTION
    ): PendingIntent? = suspendCoroutine { continuation ->
        Wallet.getPaymentsClient(
            context,
            Wallet.WalletOptions.Builder()
                .setEnvironment(environment)
                .build(),
        )
            .getPaymentCardRecognitionIntent(
                PaymentCardRecognitionIntentRequest.getDefaultInstance(),
            )
            .addOnSuccessListener { intentResponse ->
                continuation.resume(intentResponse.paymentCardRecognitionPendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e("PaymentCardRecognition", "Error getting intent: ${e.message}", e)
                continuation.resume(null)
            }
    }

    fun fromResult(activityResult: ActivityResult): Pair<String, ValidatedExpirationDate>? =
        if (activityResult.resultCode == Activity.RESULT_OK) {
            activityResult.data
                ?.let { intent ->
                    PaymentCardRecognitionResult.getFromIntent(intent)
                }
                ?.let { result ->
                    result.pan to ValidatedExpirationDate(
                        month = result.creditCardExpirationDate?.month,
                        year = result.creditCardExpirationDate?.year,
                    )
                }
        } else {
            Log.e(
                "PaymentCardRecognition",
                "card recognition canceled: ${activityResult.resultCode}",
            )
            null
        }
}
