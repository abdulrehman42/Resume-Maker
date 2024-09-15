package com.pentabit.cvmaker.resumebuilder.views.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityAddDetailResumeBinding
import com.pentabit.cvmaker.resumebuilder.databinding.AddmorealertdialogueBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.backPressedcheck
import com.pentabit.cvmaker.resumebuilder.utils.ImageSourceSelectionHelper
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
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
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDetailResume : BaseActivity() {
    private var currentFragment: AddDetailsBaseFragment<ViewBinding>? = null
    private var currentTabPosition = 0
    private lateinit var binding: ActivityAddDetailResumeBinding
    private val extraTabs = ArrayList<TabModel>()
    private val allTabs = ArrayList<TabModel>()
    lateinit var imageSourceSelectionHelper: ImageSourceSelectionHelper
    private var isCreateResume = false
    private val addDetailResumeVM: AddDetailResumeVM by viewModels()
    private var screenId = ScreenIDs.ADD_BASIC_INFO

    data class TabModel(
        val id: Int, val name: String, val icon: Drawable?
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailResumeBinding.inflate(layoutInflater)
        isCreateResume = intent.getBooleanExtra("CreateResume", false)
        bottomNavigationColor()
//        enableEdgeToEdge()
        setContentView(binding.root)
        addDetailResumeVM.isInEditMode = intent.getBooleanExtra(Constants.IS_EDIT, false)
        imageSourceSelectionHelper = ImageSourceSelectionHelper(this)
        handleAds()
        setUpTablayout()
        handleClicks()
        observeLiveData()
        openFragment(0)
    }

    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            this, binding.banner, Utils.createAdKeyFromScreenId(screenId)
        )
    }

    private fun observeLiveData() {
        addDetailResumeVM.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }

        addDetailResumeVM.informationResponse.observe(this) {
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(Constants.PROFILE_ID, it.id.toString())
            moveNext()
        }

        addDetailResumeVM.objectiveResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.educationResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.skillResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.experienceResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.referenceResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.interestResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.languageResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.projectResponse.observe(this) {
            moveNext()
        }
        addDetailResumeVM.achievementResponse.observe(this) {
            moveNext()
        }
    }

    fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        ).add(R.id.add_detail_container, fragment).addToBackStack(null)
            .commitAllowingStateLoss()
    }

    private fun handleClicks() {
        binding.includeTool.backbtn.setOnClickListener {
            onBackPressed()
        }
        binding.back.setOnClickListener {
            moveBack()
        }
        binding.btnNxt.setOnClickListener {
            if (currentFragment!!.onMoveNextClicked()) {
                moveNext()
            }
        }
        binding.nextbtn.setOnClickListener {
            if (currentFragment!!.onMoveNextClicked()) {
                moveNext()
            }
        }
        binding.addTabs.setOnClickListener { alertbox() }

    }

    override fun onBackPressed() {
        if (!supportFragmentManager.popBackStackImmediate()) {
            backPressedcheck(this,
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value == Constants.YES) {
                            finish()
                        }
                    }
                })
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onStart() {
        super.onStart()
        binding.includeTool.textView.text = getString(R.string.add_detail)
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return screenId
    }

    private fun setUpTablayout() {

//        binding.viewPagerContainer.isUserInputEnabled = false
        addItems()
        for (i in 0 until allTabs.size) {
            binding.tabLayoutAdddetail.getTabAt(i)?.view?.isClickable = false

        }
        binding.tabLayoutAdddetail.getTabAt(0)?.icon?.setTint(
            ContextCompat.getColor(
                this, R.color.white
            )
        )


        binding.tabLayoutAdddetail.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.setTint(getColor(R.color.white))
                if (tab.position < currentTabPosition || currentFragment!!.csnMoveForward()) {
                    openFragment(
                        binding.tabLayoutAdddetail.selectedTabPosition
                    )
                    currentTabPosition = tab.position

                    binding.nextbtn.text =
                        if (currentTabPosition == allTabs.size - 1) getString(R.string.done)
                        else getString(R.string.next)

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

    fun requiredItemsAddInTab(profileModelAddDetailResponse: ProfileModelAddDetailResponse?) {
        profileModelAddDetailResponse?.let {
            if (profileModelAddDetailResponse.userInterests?.size != 0) {
                addOrRemoveTab(extraTabs[0])
            }
            if (profileModelAddDetailResponse.userLanguages?.size != 0) {
                addOrRemoveTab(extraTabs[1])
            }
            if (profileModelAddDetailResponse.userProjects?.size != 0) {
                addOrRemoveTab(extraTabs[2])
            }
            if (profileModelAddDetailResponse.userAchievement?.size != 0) {
                addOrRemoveTab(extraTabs[3])

            }
        }

    }


    private fun openFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.viewPager_container, createFragment(index))
        transaction.commit()
    }

    fun moveBack() {
        if (currentTabPosition >= 0) {
            if (currentTabPosition == 1) {
                binding.btnContainer.visibility = View.INVISIBLE
                binding.btnNxt.visibility = View.VISIBLE
            }
            binding.tabLayoutAdddetail.getTabAt(currentTabPosition - 1)!!.select()
        }
    }

    fun moveNext() {
        if (!currentFragment!!.csnMoveForward()) {
            return
        }
        binding.btnContainer.visibility = View.VISIBLE
        binding.btnNxt.visibility = View.INVISIBLE
        if (currentTabPosition < allTabs.size - 1) {
            binding.tabLayoutAdddetail.getTabAt(currentTabPosition + 1)!!.select()
        } else {
            if (isCreateResume) {
                val intent = Intent(this, ResumePreviewActivity::class.java)
                intent.putExtra(Constants.IS_RESUME, true)
                startActivity(intent)
            }
            finish()
        }
    }

    fun addItems() {
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

        binding.nextbtn.text = if (currentTabPosition == allTabs.size - 1) getString(R.string.done)
        else getString(R.string.next)
    }

    private fun alertbox() {
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

        binding1.done.setOnClickListener {
            dialogBuilder.dismiss()
        }


        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(false)
        dialogBuilder.show()
    }


    inner class TabCheckListener(val id: Int) : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
            addOrRemoveTab(extraTabs[id])
        }
    }


    private fun createFragment(position: Int): Fragment {

        val tab = when (allTabs[position].id) {
            0 -> {
                InformationFragment()
            }

            1 -> {
                ObjectiveFragment()
            }

            2 -> {
                EducationFragment()
            }

            3 -> {
                SkillFragment()
            }

            4 -> {
                ExperienceFragment()
            }

            5 -> {
                ReferrenceFragment()
            }

            6 -> {
                InterestFragment()
            }

            7 -> {
                LanguageFragment()
            }

            8 -> {
                ProjectFragment()
            }

            9 -> {
                AchievementFragment()
            }

            else -> {
                InformationFragment()
            }
        }

        currentFragment = tab as AddDetailsBaseFragment<ViewBinding>
        return tab
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