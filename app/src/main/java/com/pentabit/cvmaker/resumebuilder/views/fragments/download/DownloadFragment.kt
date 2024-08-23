package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentDownloadBinding
import com.pentabit.cvmaker.resumebuilder.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.utils.Constants


class DownloadFragment :BaseFragment<FragmentDownloadBinding>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    override val inflate: Inflate<FragmentDownloadBinding>
        get() = FragmentDownloadBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.setText(getString(R.string.downloads))
        binding.includeTool.backbtn.setOnClickListener { currentActivity().finish() }
        setUpTablayout()
    }

    private fun setUpTablayout() {
        viewPager = binding.viewPager
        tabLayout = binding.TabLayout
        val vadapter =
            Vadapterswipe(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        vadapter.addFragment(ResumeFragment(), getString(R.string.resume))
        vadapter.addFragment(CatagoryDownload(), getString(R.string.cover_letter))
        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
                if (tab.position==0)
                {
                    sharePref.writeBoolean(Constants.IS_RESUME,true)

                }else{
                    sharePref.writeBoolean(Constants.IS_RESUME,false)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.off_white),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        }
        tabLayout.addOnTabSelectedListener(tabSelectedListener)
    }
}