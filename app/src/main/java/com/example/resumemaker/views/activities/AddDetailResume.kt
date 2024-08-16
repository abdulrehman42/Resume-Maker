package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewbinding.ViewBinding
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityAddDetailResumeBinding
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.CustomtablayoutBinding
import com.example.resumemaker.models.TablayoutModel
import com.example.resumemaker.views.fragments.addDetailResume.AchievementFragment
import com.example.resumemaker.views.fragments.addDetailResume.EducationFragment
import com.example.resumemaker.views.fragments.addDetailResume.ExperienceFragment
import com.example.resumemaker.views.fragments.addDetailResume.InformationFragment
import com.example.resumemaker.views.fragments.addDetailResume.InterestFragment
import com.example.resumemaker.views.fragments.addDetailResume.LanguageFragment
import com.example.resumemaker.views.fragments.addDetailResume.ObjectiveFragment
import com.example.resumemaker.views.fragments.addDetailResume.ProjectFragment
import com.example.resumemaker.views.fragments.addDetailResume.ReferrenceFragment
import com.example.resumemaker.views.fragments.addDetailResume.SkillFragment
import com.example.resumemaker.views.fragments.tablayout.VadapterSwipewithIcon
import com.google.android.material.tabs.TabLayout

class AddDetailResume : BaseActivity() {
    lateinit var currentFragment: AddDetailsBaseFragment<ViewBinding>
    private lateinit var binding: ActivityAddDetailResumeBinding
    private val extraTabs = ArrayList<TabModel>()
    private val allTabs = ArrayList<TabModel>()

    data class TabModel(
        val id: Int,
        val name: String,
        val icon: Drawable?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddDetailResumeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpTablayout()
        onclick()
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.addTabs.setOnClickListener { alertbox() }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.includeTool.textView.text = "Add Detail"
    }

    private fun setUpTablayout() {
        binding.viewPager.isUserInputEnabled = false
        addItems()
        for (i in 0 until allTabs.size)
        {
            binding.tabLayoutAdddetail.getTabAt(i)?.view?.isClickable=false

        }

        binding.tabLayoutAdddetail.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getColor(R.color.white))
                if (currentFragment.csnMoveForward()) {
                    binding.viewPager.currentItem = binding.tabLayoutAdddetail.selectedTabPosition
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getColor(R.color.grey))

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Handle tab reselection if needed
            }
        })
    }


    override fun attachViewMode() {

    }

    fun addItems() {
        binding.viewPager.adapter = MyViewPagerAdapter()
        val tabTitle = resources.getStringArray(R.array.add_details_tab)
        val tabIcons = resources.obtainTypedArray(R.array.add_details_tab_icons)

        val extraTabTitle = resources.getStringArray(R.array.extra_details_tabs)
        val extraTabIcons = resources.obtainTypedArray(R.array.extra_details_tab_icons)

        for (i in tabTitle.indices) {
            addOrRemoveTab(TabModel(i, tabTitle[i], getDrawable(tabIcons.getResourceId(i, 0))))
        }

        for (i in extraTabTitle.indices) {
            extraTabs.add(
                TabModel(
                    tabTitle.size + i,
                    extraTabTitle[i],
                    getDrawable(extraTabIcons.getResourceId(i, 0))
                )
            )
        }
    }

    fun addOrRemoveTab(tabModel: TabModel) {
        if (allTabs.contains(tabModel)) {
            binding.tabLayoutAdddetail.removeTabAt(allTabs.indexOf(tabModel))
            allTabs.remove(tabModel)
            return
        }
        allTabs.add(tabModel)
        val tab = binding.tabLayoutAdddetail.newTab()
        tab.text = tabModel.name
        tab.icon = tabModel.icon
        binding.tabLayoutAdddetail.addTab(tab)
        for (i in 0 until  allTabs.size)
        {
            binding.tabLayoutAdddetail.getTabAt(binding.viewPager.currentItem)?.view?.isClickable=false
        }
    }


    fun alertbox() {
        val binding1 = AddmorealertdialogueBinding.inflate(layoutInflater)
        val dialogBuilder = Dialog(this, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding1.root)


        // Check existing fragments and update switches accordingly

        binding1.switchid.isChecked = allTabs.contains(extraTabs[0])
        binding1.switchid1.isChecked = allTabs.contains(extraTabs[1])
        binding1.switchid2.isChecked = allTabs.contains(extraTabs[2])
        binding1.switchid3.isChecked = allTabs.contains(extraTabs[3])

        binding1.switchid.setOnCheckedChangeListener(TabCheckListener(0))
        binding1.switchid1.setOnCheckedChangeListener(TabCheckListener(1))
        binding1.switchid2.setOnCheckedChangeListener(TabCheckListener(2))
        binding1.switchid3.setOnCheckedChangeListener(TabCheckListener(3))

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }


    inner class TabCheckListener(val id: Int) : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
            addOrRemoveTab(extraTabs[id])
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

    inner class MyViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int {
            return allTabs.size
        }

        override fun createFragment(position: Int): Fragment {
            val tab = when (allTabs[position].id) {
                0 -> InformationFragment()
                1 -> ObjectiveFragment()
                2 -> EducationFragment()
                3 -> SkillFragment()
                4 -> ExperienceFragment()
                5 -> ReferrenceFragment()
                6 -> InterestFragment()
                7 -> LanguageFragment()
                8 -> ProjectFragment()
                9 -> AchievementFragment()
                else -> InformationFragment()
            }
            currentFragment = tab as AddDetailsBaseFragment<ViewBinding>
            return tab
        }

    }

}