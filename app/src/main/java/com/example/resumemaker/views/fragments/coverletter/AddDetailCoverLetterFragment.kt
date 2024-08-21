package com.example.resumemaker.views.fragments.coverletter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddDetailCoverLetterBinding
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.viewmodels.CoverLetterVM
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddDetailCoverLetterFragment : BaseFragment<FragmentAddDetailCoverLetterBinding>(){
    lateinit var coverLetterVM: CoverLetterVM
    lateinit var title:String
    lateinit var coverletter:String
    override val inflate: Inflate<FragmentAddDetailCoverLetterBinding>
        get() = FragmentAddDetailCoverLetterBinding::inflate

    override fun observeLiveData() {
        coverLetterVM.coverLetterResponse.observe(currentActivity()){
            sharePref.writeString(Constants.PROFILE_ID,it.id.toString())
            val bundle=Bundle()
            bundle.putBoolean(Constants.IS_RESUME,false)
            currentActivity().replaceChoiceFragment(R.id.nav_add_preview_resume,bundle)
        }


    }

    @SuppressLint("ResourceType")
    override fun init(savedInstanceState: Bundle?) {
        title= arguments?.getString(Constants.TITLE_DATA).toString()

        coverLetterVM=ViewModelProvider(currentActivity())[CoverLetterVM::class.java]
        binding.includeTool.apply {
            textView.text=getString(R.string.add_detail)
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.selectSamplebtn.setOnClickListener {
              currentActivity().replaceChoiceFragment(R.id.nav_sample_cover_letter)
        }
        binding.nextbtn.setOnClickListener {
            if (!binding.coverletterTextInput.text.isNullOrEmpty())
            {
                apiCall()
            }else{
                currentActivity().showToast(getString(R.string.cover_letter_missing_field))
            }
        }
        binding.coverletterTextInputLayout.setEndIconOnClickListener{
            currentActivity().replaceChoiceFragment(R.id.nav_ai_writing_coverletter_sample)
        }


    }

    private fun apiCall() {
        coverLetterVM.createCoverLetter(CoverLetterRequestModel(binding.coverletterTextInput.text.toString(),title))
    }

    override fun onResume() {
        super.onResume()
        coverletter= arguments?.getString(Constants.DATA).toString()
        if (!coverletter.isNullOrEmpty())
        {
            binding.coverletterTextInput.setText(coverletter)
        }
    }

}
