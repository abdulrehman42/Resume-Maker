package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentCoverLetterBinding
import com.pentabit.cvmaker.resumebuilder.views.fragments.tablayout.Vadapterswipe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatagoryDownload : BaseFragment<FragmentCoverLetterBinding>()
{
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
  //  lateinit var templateViewModel: TemplateViewModel
    override val inflate: Inflate<FragmentCoverLetterBinding>
        get() = FragmentCoverLetterBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        setUpTablayout()
        /*templateViewModel=ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            templateViewModel.isHide.value=true
        }*/
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