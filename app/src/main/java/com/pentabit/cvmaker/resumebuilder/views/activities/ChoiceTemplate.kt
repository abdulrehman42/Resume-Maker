package com.pentabit.cvmaker.resumebuilder.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.callbacks.OnTemplateSelected
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityChoiceTemplateBinding
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.fragments.choose.BasicFragment
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChoiceTemplate : BaseActivity(), OnTemplateSelected {
    lateinit var templateViewModel: TemplateViewModel
    lateinit var binding: ActivityChoiceTemplateBinding
    private val templatesTitle = ArrayList<String>()
    private var dataMap: Map<String, List<TemplateModel>> = HashMap<String, List<TemplateModel>>()
    private var isResume = false
    private var isCreation = false
    private var templateId = ""
    private var screenId = ScreenIDs.CHOOSE_RESUME_TEMPLATES

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceTemplateBinding.inflate(layoutInflater)
        binding.includeTool.textView.text = getString(R.string.choose_templates)
        setContentView(binding.root)
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class]
        isResume = intent.getBooleanExtra(
            Constants.IS_RESUME,
            false
        )
        isCreation = intent.getBooleanExtra(Constants.CREATION_TIME, false)
        templateId = intent.getStringExtra(Constants.TEMPLATE_ID).toString()
        if (!isResume) {
            screenId = ScreenIDs.CHOOSE_COVER_LETTER_TEMPELATES
        }
        handleLiveData()
        fetchTemplates()
        onclick()
        handleAds()
    }

    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            this,
            binding.banner,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }

    private fun handleLiveData() {
        templateViewModel.dataMap.observe(this) {
            dataMap = it
            setUpTablayout(it)
        }
        templateViewModel.isHide.observe(this) {
            if (it)
                binding.choiceTemplateContainer.isGone = true
        }
        templateViewModel.loadingState.observe(this) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
        }
    }

    private fun fetchTemplates() {
        templateViewModel.fetchTemplates(
            if (isResume)
                Constants.RESUME
            else
                Constants.COVER_LETTER
        )
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            finish()
        }
    }

    private fun setUpTablayout(map: Map<String, List<TemplateModel>>) {
        for (i in map.keys) {
            val tab = binding.TabLayout.newTab()
            tab.text = i.replaceFirstChar { it.uppercase() }
            binding.TabLayout.addTab(tab)
            templatesTitle.add(i)
        }

        binding.viewPager.adapter = MyViewPagerAdapter()

        val tabSelectedListener = object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.viewPager.currentItem = binding.TabLayout.selectedTabPosition
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        }
        binding.TabLayout.addOnTabSelectedListener(tabSelectedListener)
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.TabLayout.selectTab(binding.TabLayout.getTabAt(position)) // Synchronize TabLayout with ViewPager2
            }
        })
    }

    inner class MyViewPagerAdapter : FragmentStateAdapter(supportFragmentManager, lifecycle) {
        override fun getItemCount(): Int = templatesTitle.size

        override fun createFragment(position: Int): Fragment {
            return BasicFragment(dataMap.get(templatesTitle[position]), screenId)
        }
    }

    override fun onTemplateSelected(model: TemplateModel) {
        val check = AppsKitSDKPreferencesManager.getInstance()
            .getBooleanPreferences(Constants.IS_LOGGED, false)
        if (check) {

            if (isCreation) {
                navigateToPreviewScreen()
            } else if (isResume) {
                if (templateId != "null") {
                    navigateToPreviewScreenFinish()
                } else {
                    navigateToProfileActivity()
                }
            } else {
                if (templateId != "null") {
                    navigateToPreviewScreenFinish()
                }else {
                    navigateToCoverLetterResumeActivity()
                }
            }
        } else {
            if (isResume) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Constants.IS_RESUME, true)
                startActivity(intent)
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Constants.IS_RESUME, false)
                startActivity(intent)
            }

        }
    }

    private fun navigateToPreviewScreenFinish() {
        val intent = Intent(this, ResumePreviewActivity::class.java)
        if (isResume) {
            intent.putExtra(Constants.IS_RESUME, true)
        } else {
            intent.putExtra(Constants.IS_RESUME, false)

        }
        startActivity(intent)
        finish()
    }

    private fun navigateToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        AppsKitSDKPreferencesManager.getInstance()
            .addInPreferences(Constants.FRAGMENT_CALLED, Constants.RESUME)
        startActivity(intent)
    }

    private fun navigateToCoverLetterResumeActivity() {
        startActivity(Intent(this, CreateCoverLetterActivity::class.java))
    }

    private fun navigateToPreviewScreen() {
        val intent = Intent(this, ResumePreviewActivity::class.java)
        if (isResume) {
            intent.putExtra(Constants.IS_RESUME, true)
        } else {
            intent.putExtra(Constants.IS_RESUME, false)

        }
        startActivity(intent)
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return screenId
    }

}