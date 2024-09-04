package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddDetailCoverLetterBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.activities.ResumePreviewActivity
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddDetailCoverLetterFragment : BaseFragment<FragmentAddDetailCoverLetterBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    lateinit var title: String
    val screenId= ScreenIDs.CREATE_COVER_LETTER
    lateinit var coverletter: String
    var isCalled=false
    override val inflate: Inflate<FragmentAddDetailCoverLetterBinding>
        get() = FragmentAddDetailCoverLetterBinding::inflate

    override fun observeLiveData() {
        templateViewModel.loadingState.observe(currentActivity())
        {
            AppsKitSDKUtils.setVisibility(it,binding.loader)
        }
        templateViewModel.coverLetterResponse.observe(currentActivity()) {
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.PROFILE_ID, it.id.toString())
            isCalled=true
        }
    }

    @SuppressLint("ResourceType")
    override fun init(savedInstanceState: Bundle?) {
        title = arguments?.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.TITLE_DATA).toString()
        templateViewModel = ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        binding.includeTool.apply {
            textView.text = getString(R.string.add_detail)
            backbtn.setOnClickListener {
                templateViewModel.isHide.value = true
                currentActivity().onBackPressedDispatcher.onBackPressed()
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    templateViewModel.isHide.value=true
                }
            }
        }

        binding.selectSamplebtn.setOnClickListener {
            currentActivity().supportFragmentManager.beginTransaction().setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
                .replace(R.id.choice_template_container, CoverLetterSampleFragment())
                .addToBackStack(null)
                .commit()
        }
        binding.nextbtn.setOnClickListener {
            if (!binding.coverletterTextInput.text.isNullOrEmpty()) {
                apiCall()
                MainScope().launch {
                    delay(3000)
                    if (isCalled)
                    {
                        moveToFragment()
                    }
                }
            } else {
                currentActivity().showToast(getString(R.string.cover_letter_missing_field))
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
        coverletter = arguments?.getString(Constants.DATA).toString()
        if (coverletter != "null") {
            binding.coverletterTextInput.setText(coverletter)
        }
    }

    private fun moveToFragment() {
        val intent =Intent(currentActivity(), ResumePreviewActivity::class.java)
        intent.putExtra(Constants.IS_RESUME,false)
        intent.putExtra(Constants.CREATION_TIME,true)
        startActivity(intent)
      //  currentActivity().finish()
    }

}
