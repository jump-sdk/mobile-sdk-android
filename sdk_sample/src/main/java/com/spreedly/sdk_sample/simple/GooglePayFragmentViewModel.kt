package com.spreedly.sdk_sample.simple // ktlint-disable package-name

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spreedly.client.SpreedlyClient.Companion.newInstance
import com.spreedly.client.models.GooglePayInfo
import kotlinx.coroutines.launch

class GooglePayFragmentViewModel : ViewModel() {
    @JvmField
    var name = MutableLiveData("")
    @JvmField
    var token = MutableLiveData<String?>(null)
    @JvmField
    var error = MutableLiveData<String?>(null)
    @JvmField
    var inProgress = MutableLiveData(false)

    fun create() {
        val client = newInstance(
            "XsQXqPtrgCOnpexSwyhzN9ngr2c",
            "ghEGueczUT4BhJv54K24G6B4Oy9yWaM5R4dR2yt5gRsx3xnwbZE0OZ0mRg2zyI5g",
            true,
        )
        val names = name.value!!.split(" ".toRegex(), limit = 2).toTypedArray()
        val paymentData = """{
  "protocolVersion":"ECv2",
  "signature":"MEQCIH6Q4OwQ0jAceFEkGF0JID6sJNXxOEi4r+mA7biRxqBQAiAondqoUpU/bdsrAOpZIsrHQS9nwiiNwOrr24RyPeHA0Q\u003d\u003d",
  "intermediateSigningKey":{
    "signedKey": "{\"keyExpiration\":\"1542323393147\",\"keyValue\":\"MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAE/1+3HBVSbdv+j7NaArdgMyoSAM43yRydzqdg1TxodSzA96Dj4Mc1EiKroxxunavVIvdxGnJeFViTzFvzFRxyCw\\u003d\\u003d\"}",
    "signatures": ["MEYCIQCO2EIi48s8VTH+ilMEpoXLFfkxAwHjfPSCVED/QDSHmQIhALLJmrUlNAY8hDQRV/y1iKZGsWpeNmIP+z+tCQHQxP0v"]
  },
  "signedMessage":"{\"tag\":\"jpGz1F1Bcoi/fCNxI9n7Qrsw7i7KHrGtTf3NrRclt+U\\u003d\",\"ephemeralPublicKey\":\"BJatyFvFPPD21l8/uLP46Ta1hsKHndf8Z+tAgk+DEPQgYTkhHy19cF3h/bXs0tWTmZtnNm+vlVrKbRU9K8+7cZs\\u003d\",\"encryptedMessage\":\"mKOoXwi8OavZ\"}"
}"""
        val info = GooglePayInfo(names[0], names[1], paymentData, false)
        info.testCardNumber = "411111111111111"
        inProgress.setValue(true)
        token.postValue("")
        error.postValue("")
        viewModelScope.launch {
            val trans = client.createGooglePaymentMethod(info)
            try {
                if (trans.succeeded) {
                    Log.i("Spreedly", "trans.result.token: " + trans.result!!.token)
                    token.postValue(trans.result!!.token)
                } else {
                    Log.e("Spreedly", "trans.message: " + trans.message)
                    error.postValue(trans.message)
                }
            } finally {
                inProgress.postValue(false)
            }
        }
    }
}
