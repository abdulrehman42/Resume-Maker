package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddDetailCoverLetterBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ResumePreviewFragment
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDetailCoverLetterFragment : BaseFragment<FragmentAddDetailCoverLetterBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    lateinit var title: String
    lateinit var coverletter: String
    override val inflate: Inflate<FragmentAddDetailCoverLetterBinding>
        get() = FragmentAddDetailCoverLetterBinding::inflate

    override fun observeLiveData() {
        templateViewModel.coverLetterResponse.observe(currentActivity()) {
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID, it.id.toString())
            moveToFragment()
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
        coverletter = arguments?.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA).toString()
        if (coverletter != "null") {
            binding.coverletterTextInput.setText(coverletter)
        }
    }

    private fun moveToFragment() {
        val bundle = Bundle()
        val fragment = ResumePreviewFragment()
        bundle.putBoolean(com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME, false)

        fragment.arguments = bundle
        currentActivity().supportFragmentManager.beginTransaction().setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
            .replace(R.id.choice_template_container, fragment)
            .addToBackStack(null)
            .commit()
    }

}
