package com.pentabit.cvmaker.resumebuilder.views.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityDownloadBinding
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.views.fragments.download.DownloadViewPaperFragment
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils


class DownloadActivity : BaseActivity() {
    lateinit var binding: ActivityDownloadBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        setUpTabs()
        handleAds()
    }

    private fun init() {
        binding.includeTool.textView.setText(getString(R.string.downloads))
        binding.includeTool.backbtn.setOnClickListener { finish() }
    }

    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            this,
            binding.banner,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }

    private fun setUpTabs() {
        binding.viewPager.adapter = MyViewPagerAdapter()

        binding.tablayout.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem =
                    binding.tablayout.selectedTabPosition
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        })
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tablayout.selectTab(binding.tablayout.getTabAt(position))
            }
        })
    }

    inner class MyViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                DownloadViewPaperFragment("Resume")
            } else {
                DownloadViewPaperFragment("CoverLetter")
            }
        }

    }


    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.DOWNLOADS
    }
}