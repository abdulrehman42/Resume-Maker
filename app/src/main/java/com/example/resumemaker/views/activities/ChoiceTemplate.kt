package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityChoiceTemplateBinding
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.fragments.choose.BasicFragment
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChoiceTemplate : BaseActivity() {
    lateinit var templatesTitles: List<String>
    lateinit var templateViewModel: TemplateViewModel// by viewModels<TemplateViewModel>()
    lateinit var binding: ActivityChoiceTemplateBinding
    private val allTabs = ArrayList<TabModel>()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChoiceTemplateBinding.inflate(layoutInflater)
        binding.includeTool.textView.text = "Choose Template"
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
        templateViewModel.name.value = "basic"

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val icon = tab.icon
                icon?.setColorFilter(getColor(R.color.white), PorterDuff.Mode.SRC_IN)
                binding.viewPager.currentItem = binding.TabLayout.selectedTabPosition
                val selectedTabPosition = binding.TabLayout.selectedTabPosition
                val selectedTabName = templatesTitles[selectedTabPosition]
                templateViewModel.name.value = selectedTabName
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getColor(R.color.grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        }
        binding.TabLayout.addOnTabSelectedListener(tabSelectedListener)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.TabLayout.selectTab(binding.TabLayout.getTabAt(position)) // Synchronize TabLayout with ViewPager2
            }
        })
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
            return BasicFragment()
        }
    }

    private fun enableEdgeToEdge() {
        val window = window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        window.navigationBarColor = Color.TRANSPARENT

        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Light status bar (dark icons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags =
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Light navigation bar (dark icons)
        }
        window.decorView.systemUiVisibility = flags
    }

}