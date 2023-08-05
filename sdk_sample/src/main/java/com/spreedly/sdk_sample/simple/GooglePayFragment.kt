package com.spreedly.sdk_sample.simple

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.spreedly.sdk_sample.R

class GooglePayFragment : BillingFragmentBase() {
    private var mViewModel: GooglePayFragmentViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.google_pay_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(
            GooglePayFragmentViewModel::class.java
        )
        bindEditText(view!!.findViewById(R.id.name), mViewModel!!.name)
        bindTextView(view!!.findViewById(R.id.token), mViewModel!!.token)
        bindTextView(view!!.findViewById(R.id.error), mViewModel!!.error)
        mViewModel!!.inProgress.observe(viewLifecycleOwner) { inProgress ->
            view!!.findViewById<View>(R.id.name).isEnabled = inProgress != true
        }
        view!!.findViewById<Button>(R.id.submit)
            .setOnClickListener { b: View? -> mViewModel!!.create() }
    }

    companion object {
        @JvmStatic
        fun newInstance(): GooglePayFragment {
            return GooglePayFragment()
        }
    }
}
