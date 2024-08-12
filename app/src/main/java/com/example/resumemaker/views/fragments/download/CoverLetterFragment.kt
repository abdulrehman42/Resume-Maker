package com.example.resumemaker.views.fragments.download

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentCoverLetterBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.TemplateAdapter
import com.example.resumemaker.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout


class CoverLetterFragment : BaseFragment<FragmentCoverLetterBinding>()
{
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager

    override val inflate: Inflate<FragmentCoverLetterBinding>
        get() = FragmentCoverLetterBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        setUpTablayout()
    }

    private fun setUpTablayout() {
        viewPager = binding.viewPager
        tabLayout = binding.TabLayout
        val vadapter =
            Vadapterswipe(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        vadapter.addFragment(JPGFragment(), "JPG")
        vadapter.addFragment(PdfFragment(), "PDF")
        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.view.setBackground(ContextCompat.getDrawable(currentActivity(), R.drawable.whitbground));

                val icon = tab.icon
                icon?.setColorFilter(
                    ContextCompat.getColor(requireContext(), R.color.white),
                    PorterDuff.Mode.SRC_IN
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.view.setBackground(ContextCompat.getDrawable(currentActivity(), R.drawable.greybgradius));
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