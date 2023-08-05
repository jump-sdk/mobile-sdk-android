package com.spreedly.sdk_sample.simple // ktlint-disable package-name

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.R
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import java.util.Arrays

abstract class BillingFragmentBase : Fragment() {
    fun bindTextView(text: TextView, data: MutableLiveData<String?>) {
        text.text = data.value
        data.observe(viewLifecycleOwner) { s: String? -> text.text = s }
    }

    fun bindEditText(edit: EditText, data: MutableLiveData<String?>) {
        edit.setText(data.value)
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                data.postValue(s.toString())
            }
        })
    }

    fun bindEditNumber(edit: EditText, data: MutableLiveData<Int?>) {
        edit.setText(if (data.value == null) "" else data.value.toString())
        edit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                data.postValue(if (s.length == 0) null else s.toString().toInt())
            }
        })
    }

    fun <T> bindSpinner(edit: Spinner, data: MutableLiveData<T>, values: Array<T>) {
        edit.adapter =
            ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, values)
        edit.setSelection(Arrays.binarySearch(values, data.value))
        edit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long,
            ) {
                data.postValue(values[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
}
