package com.spreedly.sdk_sample.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.spreedly.sdk_sample.R

class SecureFormCreditCardFragment : Fragment() {
    private var mViewModel: SecureFormCreditCardViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.secure_form_credit_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            SecureFormCreditCardViewModel::class.java
        )
        mViewModel!!.layout = view!!.findViewById(R.id.credit_card_form)
        mViewModel!!.error = view!!.findViewById(R.id.error)
        mViewModel!!.token = view!!.findViewById(R.id.token)
        mViewModel!!.layout!!.setSpreedlyClient(
            "XsQXqPtrgCOnpexSwyhzN9ngr2c",
            "ghEGueczUT4BhJv54K24G6B4Oy9yWaM5R4dR2yt5gRsx3xnwbZE0OZ0mRg2zyI5g",
            true
        )
        mViewModel!!.setDefaults()
        view!!.findViewById<Button>(R.id.spreedly_cc_submit)
            .setOnClickListener { b: View? -> mViewModel!!.submitCreditCard() }
    }

    companion object {
        fun newInstance(): SecureFormCreditCardFragment {
            return SecureFormCreditCardFragment()
        }
    }
}