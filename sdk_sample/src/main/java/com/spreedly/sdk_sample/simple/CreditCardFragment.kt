package com.spreedly.sdk_sample.simple // ktlint-disable package-name

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import com.spreedly.sdk_sample.R

class CreditCardFragment : BillingFragmentBase() {
    private var mViewModel: CreditCardFragmentViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.credit_card_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            CreditCardFragmentViewModel::class.java,
        )
        bindEditText(requireView().findViewById(R.id.name), mViewModel!!.name)
        bindEditText(requireView().findViewById(R.id.cc), mViewModel!!.cc)
        bindEditNumber(requireView().findViewById(R.id.year), mViewModel!!.year)
        bindEditNumber(requireView().findViewById(R.id.month), mViewModel!!.month)
        bindEditText(requireView().findViewById(R.id.cvv), mViewModel!!.cvv)
        bindTextView(requireView().findViewById(R.id.token), mViewModel!!.token)
        bindTextView(requireView().findViewById(R.id.error), mViewModel!!.error)
        mViewModel!!.inProgress.observe(viewLifecycleOwner) { inProgress ->
            requireView().findViewById<View>(R.id.name).isEnabled = inProgress != true
            requireView().findViewById<View>(R.id.cc).isEnabled = inProgress != true
            requireView().findViewById<View>(R.id.cvv).isEnabled = inProgress != true
        }
        requireView().findViewById<Button>(R.id.submit)
            .setOnClickListener { mViewModel!!.create() }
    }

    companion object {
        @JvmStatic
        fun newInstance(): CreditCardFragment {
            return CreditCardFragment()
        }
    }
}
