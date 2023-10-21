package com.spreedly.sdk_sample.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.spreedly.sdk_sample.R
import com.spreedly.sdk_sample.simple.SimplePagerAdapter

class SimpleExamplesFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_core_api, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sectionsPagerAdapter = SimplePagerAdapter(childFragmentManager)
        val viewPager = requireView().findViewById<ViewPager>(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs = requireView().findViewById<TabLayout>(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
    }

    companion object {
        @JvmStatic
        fun newInstance(): SimpleExamplesFragment {
            return SimpleExamplesFragment()
        }
    }
}
