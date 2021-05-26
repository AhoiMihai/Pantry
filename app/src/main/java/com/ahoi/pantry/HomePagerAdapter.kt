package com.ahoi.pantry

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.ahoi.pantry.common.uistuff.HomeStyleFragment

class HomePagerAdapter(
    private val fragments: List<HomeStyleFragment>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int): HomeStyleFragment {
        return fragments[position]
    }

    fun fabClicked(position: Int) {
        fragments[position].activityFabClicked()
    }

    fun pageTitleAt(position: Int): Int {
        return fragments[position].titleResId
    }
}