package com.spreedly.sdk_sample.simple // ktlint-disable package-name

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SimplePagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return "Credit Card"
            1 -> return "Google Pay"
        }
        throw IndexOutOfBoundsException()
    }

    override fun getItem(position: Int): Fragment {
        when (position) {
            0 -> return CreditCardFragment.newInstance()
            1 -> return GooglePayFragment.newInstance()
        }
        throw IndexOutOfBoundsException()
    }

    override fun getCount(): Int {
        return 2
    }
}
