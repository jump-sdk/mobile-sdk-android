package com.spreedly.sdk_sample.simple

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spreedly.client.SpreedlyClient.Companion.newInstance
import com.spreedly.client.models.CreditCardInfo
import com.spreedly.client.models.results.PaymentMethodResult
import com.spreedly.client.models.results.TransactionResult
import kotlinx.coroutines.launch

class CreditCardFragmentViewModel : ViewModel() {
    @JvmField
    var name = MutableLiveData("")
    @JvmField
    var cc = MutableLiveData("")
    @JvmField
    var cvv = MutableLiveData("")
    @JvmField
    var year = MutableLiveData<Int?>(null)
    @JvmField
    var month = MutableLiveData<Int?>(null)
    @JvmField
    var inProgress = MutableLiveData<Boolean>(false)
    @JvmField
    var token = MutableLiveData<String?>(null)
    @JvmField
    var error = MutableLiveData<String?>(null)

    fun create() {
        val client = newInstance("", "", true)
        val info = CreditCardInfo(
            fullName = name.value,
            firstName = null,
            lastName = null,
            number = client.createString(cc.value!!),
            verificationValue = client.createString(cvv.value!!),
            year = year.value ?: 0,
            month = month.value ?: 0,
        )
        inProgress.setValue(true)
        token.postValue("")
        error.postValue("")
        viewModelScope.launch {
            val trans = try {
                client.createCreditCardPaymentMethod(info)
            } catch (e: Exception) {
                error.postValue("UNEXPECTED ERROR: " + e.message)
                return@launch
            }
            try {
                if (trans.succeeded) {
                    Log.i("Spreedly", "trans.result.token: " + trans.result!!.token)
                    token.postValue(trans.result!!.token)
                } else {
                    Log.e("Spreedly", "trans.message: " + trans.errors?.firstOrNull()?.message)
                    error.postValue(trans.errors?.firstOrNull()?.message ?: "UNEXPECTED ERROR")
                }
            } finally {
                inProgress.postValue(false)
            }
        }
    }
}
