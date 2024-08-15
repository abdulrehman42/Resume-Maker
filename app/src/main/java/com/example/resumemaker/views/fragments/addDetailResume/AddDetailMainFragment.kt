package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.FragmentAddDetailMainBinding
import com.example.resumemaker.models.TablayoutModel
import com.example.resumemaker.views.fragments.tablayout.VadapterSwipewithIcon
import com.google.android.material.tabs.TabLayout
import okhttp3.internal.notify

class AddDetailMainFragment : BaseFragment<FragmentAddDetailMainBinding>() {
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager
    lateinit var currentFragment: Fragment
    lateinit var tabView: View
    lateinit var tabText: TextView
    private val addedTabs = mutableSetOf<String>()

    lateinit var tabIcon: ImageView
    var itemArraylist = ArrayList<TablayoutModel>()
    lateinit var vadapter: VadapterSwipewithIcon
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
        binding.addTabs.setOnClickListener {
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
        tabLayout = binding.tabLayoutAdddetail
        vadapter =
            VadapterSwipewithIcon(childFragmentManager) // Use childFragmentManager for fragments within a fragment
        addItems()
        tabView = LayoutInflater.from(requireActivity()).inflate(R.layout.customtablayout, null)
        tabIcon = tabView.findViewById(R.id.nav_icon)
        tabText = tabView.findViewById(R.id.nav_label)
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // Handle page scrolling
                if (position>=3)
                {
                    binding.addTabs.isGone=false
                }
                Log.d("PageScroll", "Page scrolled: $position, Offset: $positionOffset")
            }

            override fun onPageSelected(position: Int) {
                // Handle page selection
                if (position>=3)
                {
                    binding.addTabs.isGone=false
                }
                Log.d("PageScroll", "Page selected: $position")
            }

            override fun onPageScrollStateChanged(state: Int) {
                // Handle page scroll state changes
                           }
        })

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
                val position = tab.position
                val currentFragment = vadapter.getFragment(viewPager.currentItem)

                // Check if the current fragment meets the condition before switching
                if (currentFragment is InformationFragment) {
                    if ((currentFragment as InformationFragment).isConditionMet()) {
                        viewPager.currentItem = tab.position  // Allow tab switch
                    } else {
                        tabhost.getTabAt(viewPager.currentItem)!!.select()

                    }
                }
                if (currentFragment is ObjectiveFragment) {
                    if ((currentFragment as ObjectiveFragment).isConditionMet()) {
                        viewPager.currentItem = tab.position  // Allow tab switch
                    } else {
                        tabhost.getTabAt(viewPager.currentItem)!!.select()

                    }
                }

                // If conditions are met or the fragment does not need validation, allow tab switch
                viewPager.currentItem = position

                // Update tab icon and text
                val icon = tab.icon
                icon?.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white), PorterDuff.Mode.SRC_IN)
                tabText?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

                // Optionally change the icon or text when selected
                tabIcon.setColorFilter(ContextCompat.getColor(currentActivity(), R.color.grey))
                tabText.setTextColor(ContextCompat.getColor(currentActivity(), R.color.grey))
                if (tab.position >= 3) {
                    // Open a dialog or a new activity to display additional tabs
                    binding.addTabs.isGone = false
                } else {
                    binding.addTabs.isGone = true

                }
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

    fun addItems() {

        vadapter.addFragment(InformationFragment(), "Info", R.drawable.info)
        vadapter.addFragment(ObjectiveFragment(), "Objectives", R.drawable.objectives)
        vadapter.addFragment(EducationFragment(), "Education", R.drawable.education)
        vadapter.addFragment(SkillFragment(), "Skills", R.drawable.skill)
        vadapter.addFragment(ExperienceFragment(), "Experience", R.drawable.experience)
        vadapter.addFragment(ReferrenceFragment(), "Reference", R.drawable.referrence)


        viewPager.adapter = vadapter
        tabLayout.setupWithViewPager(viewPager)
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val tabView =
                LayoutInflater.from(requireActivity()).inflate(R.layout.customtablayout, null)
            val tabIcon = tabView.findViewById<ImageView>(R.id.nav_icon)
            val tabText = tabView.findViewById<TextView>(R.id.nav_label)
            tabText.setTextColor(ContextCompat.getColor(currentActivity(), R.color.grey))

            // Customize tab icon and text for each tab
            when (i) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.info)
                    tabText.text = "Info"
                }

                1 -> {
                    tabIcon.setImageResource(R.drawable.objectives)
                    tabText.text = "Objective"
                }

                2 -> {
                    tabIcon.setImageResource(R.drawable.education)
                    tabText.text = "Education"
                }

                3 -> {
                    tabIcon.setImageResource(R.drawable.skill)
                    tabText.text = "Skills"
                }

                4 -> {
                    tabIcon.setImageResource(R.drawable.experience)
                    tabText.text = "Experience"
                }

                5 -> {
                    tabIcon.setImageResource(R.drawable.referrence)
                    tabText.text = "Referrence"
                }
            }
            if (i == 6) {
                setNewTab(currentFragment)
            }
            if (i == 7) {
                setNewTab(currentFragment)
            }
            if (i == 8) {
                setNewTab(currentFragment)
            }
            if (i == 9) {
                setNewTab(currentFragment)
            }
            tab?.customView = tabView

        }

    }

    fun setNewTab(currentFragment: Fragment) {

        if (currentFragment is InterestFragment) {
            tabIcon.setImageResource(R.drawable.interest)
            tabText.text = "Interests"

        }
        if (currentFragment is InterestFragment) {
            tabIcon.setImageResource(R.drawable.language)
            tabText.text = "Language"

        }
        if (currentFragment is InterestFragment) {
            tabIcon.setImageResource(R.drawable.project)
            tabText.text = "Projects"

        }
        if (currentFragment is InterestFragment) {
            tabIcon.setImageResource(R.drawable.achievment)
            tabText.text = "Achievements"

        }
    }


    fun alertbox() {
        val binding1 = AddmorealertdialogueBinding.inflate(layoutInflater)
        val dialogBuilder = Dialog(currentActivity(), R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding1.root)
        binding1.switchid.isChecked = vadapter.checkFragments().contains("Interests")
        binding1.switchid1.isChecked = vadapter.checkFragments().contains("Language")
        binding1.switchid2.isChecked = vadapter.checkFragments().contains("Projects")
        binding1.switchid3.isChecked = vadapter.checkFragments().contains("Achievements")

        // Check existing fragments and update switches accordingly
        if (vadapter.checkFragments().contains("Interests")) {
            binding1.switchid.isChecked = true
        }
        if (vadapter.checkFragments().contains("Language")) {
            binding1.switchid1.isChecked = true
        }
        if (vadapter.checkFragments().contains("Projects")) {
            binding1.switchid2.isChecked = true
        }
        if (vadapter.checkFragments().contains("Achievements")) {
            binding1.switchid3.isChecked = true
        }

        // Set up switches for adding new fragments
        binding1.switchid.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vadapter.addFragment(InterestFragment(), "Interests", R.drawable.interest)
                addedTabs.add("Interests")
            } else {
                vadapter.removeFragment("Interests")
                addedTabs.remove("Interests")
            }
            vadapter.notifyDataSetChanged()
            reapplyCustomTabs() // Reapply custom views to tabs
        }
        binding1.switchid1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vadapter.addFragment(LanguageFragment(), "Language", R.drawable.language)
                addedTabs.add("Language")
            } else {
                vadapter.removeFragment("Language")
                addedTabs.remove("Language")
            }
            vadapter.notifyDataSetChanged()
            reapplyCustomTabs() // Reapply custom views to tabs
        }
        binding1.switchid2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vadapter.addFragment(ProjectFragment(), "Projects", R.drawable.project)
                addedTabs.add("Projects")
            } else {
                vadapter.removeFragment("Projects")
                addedTabs.remove("Projects")
            }
            vadapter.notifyDataSetChanged()
            reapplyCustomTabs() // Reapply custom views to tabs
        }
        binding1.switchid3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                vadapter.addFragment(AchievementFragment(), "Achievements", R.drawable.achievment)
                addedTabs.add("Achievements")
            } else {
                vadapter.removeFragment("Achievements")
                addedTabs.remove("Achievements")
            }
            vadapter.notifyDataSetChanged()
            reapplyCustomTabs() // Reapply custom views to tabs
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    private fun reapplyCustomTabs() {
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val tabView =
                LayoutInflater.from(requireActivity()).inflate(R.layout.customtablayout, null)
            val tabIcon = tabView.findViewById<ImageView>(R.id.nav_icon)
            val tabText = tabView.findViewById<TextView>(R.id.nav_label)

            // Default tab customization
            when (i) {
                0 -> {
                    tabIcon.setImageResource(R.drawable.info)
                    tabText.text = "Info"
                }

                1 -> {
                    tabIcon.setImageResource(R.drawable.objectives)
                    tabText.text = "Objective"
                }

                2 -> {
                    tabIcon.setImageResource(R.drawable.education)
                    tabText.text = "Education"
                }

                3 -> {
                    tabIcon.setImageResource(R.drawable.skill)
                    tabText.text = "Skills"
                }

                4 -> {
                    tabIcon.setImageResource(R.drawable.experience)
                    tabText.text = "Experience"
                }

                5 -> {
                    tabIcon.setImageResource(R.drawable.referrence)
                    tabText.text = "Reference"
                }

                else -> {
                    // Handle dynamically added tabs
                    val tabName =
                        vadapter.getFragmentName(i) // Ensure this method gets the tab name

                    when (tabName) {
                        "Interests" -> {
                            tabIcon.setImageResource(R.drawable.interest)
                            tabText.text = "Interests"
                        }

                        "Language" -> {
                            tabIcon.setImageResource(R.drawable.language)
                            tabText.text = "Language"
                        }

                        "Projects" -> {
                            tabIcon.setImageResource(R.drawable.project)
                            tabText.text = "Projects"
                        }

                        "Achievements" -> {
                            tabIcon.setImageResource(R.drawable.achievment)
                            tabText.text = "Achievements"
                        }

                        else -> {
                            // Handle unknown tabs
                            tabText.text = "Unknown"
                        }
                    }
                }
            }

            tab?.customView = tabView
        }

    }
}