package com.cafstone.application.view.Onboard

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = OnboardingFragment()
            1 -> fragment = Onboarding1Fragment()
            2 -> fragment = Onboarding2Fragment()
        }
        return fragment as Fragment
    }

}