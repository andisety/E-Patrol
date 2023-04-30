package com.andi.epatrol.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.andi.epatrol.ui.home.ItemFragment.Companion.ARG_SECTION_NUMBER

class SectionPageAdapter(fm: Fragment): FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = ItemFragment()
        fragment.arguments = Bundle().apply{
            putInt(ARG_SECTION_NUMBER, position + 1)
        }
        return fragment
    }
}