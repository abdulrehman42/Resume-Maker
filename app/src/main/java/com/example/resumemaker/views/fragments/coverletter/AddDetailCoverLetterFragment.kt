package com.example.resumemaker.views.fragments.coverletter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddDetailCoverLetterBinding
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.fragments.addDetailResume.ResumePreviewFragment
import com.google.android.material.textfield.TextInputLayout
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
            sharePref.writeString(Constants.PROFILE_ID, it.id.toString())
            moveToFragment()
        }


    }


    @SuppressLint("ResourceType")
    override fun init(savedInstanceState: Bundle?) {
        title = arguments?.getString(Constants.TITLE_DATA).toString()
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
        coverletter = arguments?.getString(Constants.DATA).toString()
        if (coverletter != "null") {
            binding.coverletterTextInput.setText(coverletter)
        }
    }

    private fun moveToFragment() {
        val bundle = Bundle()
        val fragment = ResumePreviewFragment()
        bundle.putBoolean(Constants.IS_RESUME, false)

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
