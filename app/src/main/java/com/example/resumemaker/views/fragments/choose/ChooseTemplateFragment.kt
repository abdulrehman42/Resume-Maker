package com.example.resumemaker.views.fragments.choose

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentChooseTemplateBinding
import com.example.resumemaker.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout


class ChooseTemplateFragment : BaseFragment<FragmentChooseTemplateBinding>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    override val inflate: Inflate<FragmentChooseTemplateBinding>
        get() = FragmentChooseTemplateBinding::inflate




    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.setText("Choice Template")
        binding.includeTool.backbtn.setOnClickListener { currentActivity().finish() }
        setUpTablayout()
    }
    private fun setUpTablayout() {
        viewPager = binding.viewPager
        tabLayout = binding.TabLayout
        val vadapter =
            Vadapterswipe(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        vadapter.addFragment(BasicFragment(), "Basic ")
        vadapter.addFragment(StandardFragment(), "Standard")
        vadapter.addFragment(PremiumFragment(), "Premuim")
        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)
        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
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