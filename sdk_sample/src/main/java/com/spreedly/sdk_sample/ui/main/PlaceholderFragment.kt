package com.spreedly.sdk_sample.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.spreedly.sdk_sample.R

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_main, container, false)
        val textView = root.findViewById<TextView>(R.id.section_label)
        textView.text = "COMING SOON"
        return root
    }

    companion object {
        fun newInstance(): PlaceholderFragment {
            return PlaceholderFragment()
        }
    }
}