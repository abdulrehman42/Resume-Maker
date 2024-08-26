package com.pentabit.cvmaker.resumebuilder.views.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityAddDetailResumeBinding
import com.pentabit.cvmaker.resumebuilder.databinding.AddmorealertdialogueBinding
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.AchievementFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.EducationFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ExperienceFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.InformationFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.InterestFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.LanguageFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ObjectiveFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ProjectFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ReferrenceFragment
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.SkillFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDetailResume : BaseActivity() {
    lateinit var currentFragment: AddDetailsBaseFragment<ViewBinding>
    private lateinit var binding: ActivityAddDetailResumeBinding
    private val extraTabs = ArrayList<TabModel>()
    private val allTabs = ArrayList<TabModel>()
    lateinit var addDetailResumeVM: AddDetailResumeVM

    data class TabModel(
        val id: Int,
        val name: String,
        val icon: Drawable?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailResumeBinding.inflate(layoutInflater)
        bottomNavigationColor()
        enableEdgeToEdge()
        setContentView(binding.root)
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]

        setUpTablayout()
        onclick()
        observeLiveData()


    }

    private fun observeLiveData() {
        addDetailResumeVM.isHide.observe(this) {
            if (it) {
                binding.addDetailContainer.isGone = true
            } else {
                binding.addDetailContainer.isGone = false
            }
        }
        addDetailResumeVM.fragment.observe(this) {
            supportFragmentManager.beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
                .replace(R.id.add_detail_container, it)
                .addToBackStack(null)
                .commit()

        }
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            sharePref.deleteItemSharePref(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA_PROFILE)
            finish()
        }
        onBackPressedDispatcher.addCallback{
            sharePref.deleteItemSharePref(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA_PROFILE)
            finish()}
        binding.addTabs.setOnClickListener { alertbox() }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.includeTool.textView.text = getString(R.string.add_detail)
    }

    private fun setUpTablayout() {
        binding.viewPagerContainer.isUserInputEnabled = false
        addItems()
        for (i in 0 until allTabs.size) {
            binding.tabLayoutAdddetail.getTabAt(i)?.view?.isClickable = false

        }

        binding.tabLayoutAdddetail.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getColor(R.color.white))
                if (currentFragment.csnMoveForward()) {
                    binding.viewPagerContainer.setCurrentItem(binding.tabLayoutAdddetail.selectedTabPosition, true)
                   /* binding.viewPagerContainer.currentItem =
                        binding.tabLayoutAdddetail.selectedTabPosition*/
                } else {
                    showToast(getString(R.string.field_missing_error))
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
        binding.viewPagerContainer.adapter = MyViewPagerAdapter()
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

    @SuppressLint("NotifyDataSetChanged")
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
        /*for (i in 0 until allTabs.size) {
            binding.tabLayoutAdddetail.getTabAt(binding.viewPagerContainer.currentItem)?.view?.isClickable =
                false
        }*/
        binding.viewPagerContainer.adapter?.notifyDataSetChanged()

    }


    fun alertbox() {
        val binding1 = AddmorealertdialogueBinding.inflate(layoutInflater)
        val dialogBuilder = Dialog(this, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding1.root)

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


    private fun enableEdgeToEdge() {
        // Set the decor view to enable full-screen layout
        val window = window

        // Make sure that the content extends into the system bars (status bar and navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        // Set system bars to be transparent
        window.statusBarColor = ContextCompat.getColor(this,R.color.navy_blue)
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