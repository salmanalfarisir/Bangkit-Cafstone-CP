package com.cafstone.application.view.detail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class ViewPagerAdapter(activity: DetailActivity) :
    FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return RingkasanFragment()
            1 -> return UlasanFragment()
            2 -> return DetailFragment()
        }
        return RingkasanFragment()
    }

    override fun getItemCount(): Int {
        return 3
    }
}