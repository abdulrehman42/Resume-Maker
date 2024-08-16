package com.example.resumemaker.views.activities

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityChoiceTemplateBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.activities.AddDetailResume.TabModel
import com.example.resumemaker.views.fragments.addDetailResume.AchievementFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddAchievementsFRagment
import com.example.resumemaker.views.fragments.addDetailResume.AddEducation
import com.example.resumemaker.views.fragments.addDetailResume.AddExperienceFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddInterestFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddLanguageFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddProjectFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddSkillFragment
import com.example.resumemaker.views.fragments.addDetailResume.EducationFragment
import com.example.resumemaker.views.fragments.addDetailResume.ExperienceFragment
import com.example.resumemaker.views.fragments.addDetailResume.InformationFragment
import com.example.resumemaker.views.fragments.addDetailResume.InterestFragment
import com.example.resumemaker.views.fragments.addDetailResume.LanguageFragment
import com.example.resumemaker.views.fragments.addDetailResume.ObjectiveFragment
import com.example.resumemaker.views.fragments.addDetailResume.ProjectFragment
import com.example.resumemaker.views.fragments.addDetailResume.ReferrenceFragment
import com.example.resumemaker.views.fragments.addDetailResume.SkillFragment
import com.example.resumemaker.views.fragments.choose.BasicFragment
import com.example.resumemaker.views.fragments.choose.PremiumFragment
import com.example.resumemaker.views.fragments.choose.StandardFragment
import com.example.resumemaker.views.fragments.tablayout.Vadapterswipe
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChoiceTemplate : BaseActivity() {
    lateinit var templatesTitles:List<String>
    lateinit var templateViewModel : TemplateViewModel// by viewModels<TemplateViewModel>()
    lateinit var binding: ActivityChoiceTemplateBinding
    private val allTabs = ArrayList<TabModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivityChoiceTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class]
        setUpTablayout()
        onclick()
    }

    override fun attachViewMode() {

    }
    data class TabModel(
        val id: Int,
        val name: String,
    )

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun setUpTablayout() {
        templatesTitles = resources.getStringArray(R.array.template_titles).toList()
        for (i in templatesTitles.indices) {
            addOrRemoveTab(TabModel(i, templatesTitles[i]))
        }

        binding.viewPager.adapter = MyViewPagerAdapter()

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                binding.viewPager.currentItem = binding.TabLayout.selectedTabPosition
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(getColor(R.color.off_white), PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        }
        binding.TabLayout.addOnTabSelectedListener(tabSelectedListener)
    }

    fun addOrRemoveTab(tabModel: TabModel) {
        if (allTabs.contains(tabModel)) {
            binding.TabLayout.removeTabAt(allTabs.indexOf(tabModel))
            allTabs.remove(tabModel)
            return
        }
        allTabs.add(tabModel)
        val tab = binding.TabLayout.newTab()
        tab.text = tabModel.name
        binding.TabLayout.addTab(tab)
    }

    inner class MyViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int = allTabs.size

        override fun createFragment(position: Int): Fragment {
            templateViewModel.name.value = templatesTitles[position]
            return BasicFragment()
        }
    }
    private fun enableEdgeToEdge() {
// Set the decor view to enable full-screen layout
        val window = window

// Make sure that the content extends into the system bars (status bar and navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

// Set system bars to be transparent
        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        window.navigationBarColor = Color.TRANSPARENT

// Optionally, handle light or dark mode for the status bar icons
        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Light status bar (dark icons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags =
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Light navigation bar (dark icons)
        }
        window.decorView.systemUiVisibility = flags
    }

}