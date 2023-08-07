package com.spreedly.sdk_sample.ui.main

import android.content.Context
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.spreedly.sdk_sample.R
import com.spreedly.sdk_sample.compose.ComposeWidgetsFragment
import com.spreedly.sdk_sample.widget.SecureFormCreditCardFragment

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SimpleExamplesFragment.newInstance()
            1 -> SecureFormCreditCardFragment.newInstance()
            2 -> ComposeWidgetsFragment.newInstance()
            else -> PlaceholderFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_core_api,
            R.string.tab_secure_widgets,
            R.string.tab_compose_widgets,
        )
    }
}
