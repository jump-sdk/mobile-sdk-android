package com.spreedly.sdk_sample.widget // ktlint-disable package-name

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spreedly.sdk_sample.R
import com.spreedly.securewidgets.SecureFormLayout
import kotlinx.coroutines.launch

class SecureFormCreditCardViewModel : ViewModel() {
    @JvmField
    var token: TextView? = null

    var layout: SecureFormLayout? = null

    @JvmField
    var error: TextView? = null

    // TODO: Implement the ViewModel
    fun submitCreditCard() {
        token?.text = ""
        error?.text = ""
        viewModelScope.launch {
            val transaction = try {
                layout?.createCreditCardPaymentMethod()
            } catch (e: Exception) {
                error?.setText(R.string.generic_error)
                return@launch
            }

            transaction?.result?.token?.let {
                token?.text = it
            } ?: run {
                transaction?.errors?.firstOrNull()?.let {
                    error?.text = it.message
                } ?: run {
                    error?.setText(R.string.generic_error)
                }
            }
        }
    }

    fun setDefaults() {}
}
