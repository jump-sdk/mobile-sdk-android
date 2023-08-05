package com.spreedly.sdk_sample.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.spreedly.sdk_sample.R

class CreditCardFragment : BillingFragmentBase() {
    private var mViewModel: CreditCardFragmentViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.credit_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            CreditCardFragmentViewModel::class.java
        )
        bindEditText(view!!.findViewById(R.id.name), mViewModel!!.name)
        bindEditText(view!!.findViewById(R.id.cc), mViewModel!!.cc)
        bindEditNumber(view!!.findViewById(R.id.year), mViewModel!!.year)
        bindEditNumber(view!!.findViewById(R.id.month), mViewModel!!.month)
        bindEditText(view!!.findViewById(R.id.cvv), mViewModel!!.cvv)
        bindTextView(view!!.findViewById(R.id.token), mViewModel!!.token)
        bindTextView(view!!.findViewById(R.id.error), mViewModel!!.error)
        mViewModel!!.inProgress.observe(viewLifecycleOwner) { inProgress ->
            view!!.findViewById<View>(R.id.name).isEnabled = inProgress != true
            view!!.findViewById<View>(R.id.cc).isEnabled = inProgress != true
            view!!.findViewById<View>(R.id.cvv).isEnabled = inProgress != true
        }
        view!!.findViewById<Button>(R.id.submit)
            .setOnClickListener { b: View? -> mViewModel!!.create() }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CreditCardFragment {
            return CreditCardFragment()
        }
    }
}
