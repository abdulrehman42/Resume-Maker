package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.callbacks.OnCoverLetterSampleSelected
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityCreateCoverLetterBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.LAST_COVER_LETTER
import com.pentabit.cvmaker.resumebuilder.utils.Constants.LAST_COVER_LETTER_TITLE
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter.CoverLetterSampleFragment
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateCoverLetterActivity : BaseActivity(), OnCoverLetterSampleSelected {

    lateinit var binding: ActivityCreateCoverLetterBinding
    private lateinit var templateViewModel: TemplateViewModel
    lateinit var title: String
    lateinit var coverletter: String
    private var isEdit = false
    private val screenId = ScreenIDs.CREATE_COVER_LETTER

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCoverLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isEdit = intent.getBooleanExtra(Constants.IS_EDIT, false)
        if (isEdit) {
            onCoverLetterSampleSelected(
                AppsKitSDKPreferencesManager.getInstance()
                    .getStringPreferences(LAST_COVER_LETTER_TITLE),
                AppsKitSDKPreferencesManager.getInstance().getStringPreferences(LAST_COVER_LETTER)
            )
        }
        init()
        handleClicks()
        observeLiveData()
        handleAds()
    }

    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            this,
            binding.banner,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }

    fun init() {
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        binding.includeTool.apply {
            textView.text = getString(R.string.add_detail)
            backbtn.setOnClickListener {
                templateViewModel.isHide.value = true
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun handleClicks() {
        binding.selectSamplebtn.setOnClickListener {
            supportFragmentManager.beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
                .add(R.id.choice_template_container, CoverLetterSampleFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.nextbtn.setOnClickListener {
            if (!binding.coverletterTextInput.text.isNullOrEmpty()) {
                apiCall()
            } else {
                showToast(getString(R.string.cover_letter_missing_field))
            }
        }
    }

    private fun apiCall() {
        templateViewModel.createCoverLetter(
            CoverLetterRequestModel(
                binding.coverletterTextInput.text.toString(),
                title
            )
        )
    }

    private fun observeLiveData() {
        templateViewModel.coverLetterResponse.observe(this) {
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(Constants.PROFILE_ID, it.id.toString())
            moveToFragment()
        }
    }

    private fun moveToFragment() {
        val intent = Intent(this, ResumePreviewActivity::class.java)
        intent.putExtra(Constants.IS_RESUME, false)
        intent.putExtra(Constants.CREATION_TIME, true)
        startActivity(intent)
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(LAST_COVER_LETTER_TITLE, title)
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(LAST_COVER_LETTER, coverletter)
        finish()
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }


    override fun getScreenId(): ScreenIDs {
        return screenId
    }

    override fun onCoverLetterSampleSelected(title: String, sampleText: String) {
        this.title = title
        this.coverletter = sampleText
        binding.coverletterTextInput.setText(coverletter)
        supportFragmentManager.popBackStackImmediate()
    }
}