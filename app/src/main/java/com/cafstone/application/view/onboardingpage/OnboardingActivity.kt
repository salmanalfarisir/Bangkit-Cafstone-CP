package com.cafstone.application.view.onboardingpage

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.cafstone.application.R
import com.cafstone.application.databinding.ActivityOnboardingBinding
import com.cafstone.application.view.welcome.WelcomeActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tabLayout: TabLayout = findViewById(R.id.tablayout)
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.frame_container)
        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
        }.attach()

        binding.nextButton.setOnClickListener {
            if (viewPager.currentItem < viewPager.adapter!!.itemCount - 1) {
                viewPager.currentItem += 1
            } else if (viewPager.currentItem == viewPager.adapter!!.itemCount - 1) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }
    }
}