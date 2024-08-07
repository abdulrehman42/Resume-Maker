package com.example.resumemaker.views.fragments.adddetail

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.FragmentAddDetailMainBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout

class AddDetailMainFragment : BaseFragment<FragmentAddDetailMainBinding>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var vadapter:Vadapterswipe
    override val inflate: Inflate<FragmentAddDetailMainBinding>
        get() = FragmentAddDetailMainBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setUpTablayout()
        onclick()
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.floatingActionButton.setOnClickListener {
            alertbox()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.includeTool.textView.text = "Add Detail"
    }

    private fun setUpTablayout() {
        viewPager = binding.viewPager
        tabLayout = binding.TabLayout
        vadapter =
            Vadapterswipe(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        vadapter.addFragment(InformationFragment(), "Info")
        vadapter.addFragment(ObjectiveFragment(), "Objectives")
        vadapter.addFragment(EducationFragment(), "Education")
        vadapter.addFragment(SkillFragment(), "Skill")
        vadapter.addFragment(ExperienceFragment(), "Experience")
        vadapter.addFragment(ReferrenceFragment(), "Reference")
        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            if (tab != null) {
                tab.customView = Helper.getTabView(requireActivity(), i)
            }
            if (i==5)
            {
            }
        }


        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.let {

                    if (it.position >= 3) {
                        // Open a dialog or a new activity to display additional tabs
                        binding.floatingActionButton.isGone=false
                    }else{
                        binding.floatingActionButton.isGone=true

                    }
                }
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

    fun alertbox(
    ) {


        val binding1=AddmorealertdialogueBinding.inflate(layoutInflater)
        val dialogBuilder = Dialog(currentActivity(),R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding1.root)
       binding1.switchid.setOnClickListener {
           if (binding1.switchid.isChecked) {
               vadapter.addFragment(InterestFragment(), "Interest")
               viewPager.adapter = vadapter


           }
       }
        binding1.switchid1.setOnClickListener {
            if (binding1.switchid1.isChecked) {
                vadapter.addFragment(LanguageFragment(), "Language")
                viewPager.adapter = vadapter

            }
        }
        binding1.switchid2.setOnClickListener {
            if (binding1.switchid2.isChecked) {
                vadapter.addFragment(ProjectFragment(), "Project")

                viewPager.adapter = vadapter

            }
        }
        binding1.switchid3.setOnClickListener {
            if (binding1.switchid3.isChecked) {
                vadapter.addFragment(AchievementFragment(), "Achievement")
                viewPager.adapter = vadapter

            }
        }

        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

}