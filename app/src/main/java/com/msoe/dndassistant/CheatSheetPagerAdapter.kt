package com.msoe.dndassistant

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CheatSheetPagerAdapter (
    fragment: Fragment,
    private val titles: List<String>,
    private val content: List<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = titles.size

    override fun createFragment(position: Int): Fragment {
        return CheatSheetPageFragment.newInstance(titles[position], content[position])
    }
}