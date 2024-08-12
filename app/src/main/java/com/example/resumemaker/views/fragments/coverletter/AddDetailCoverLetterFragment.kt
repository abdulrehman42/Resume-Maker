package com.example.resumemaker.views.fragments.coverletter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddDetailCoverLetterBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper.dpToPx
import com.google.android.material.textfield.TextInputLayout


class AddDetailCoverLetterFragment : BaseFragment<FragmentAddDetailCoverLetterBinding>(){
    override val inflate: Inflate<FragmentAddDetailCoverLetterBinding>
        get() = FragmentAddDetailCoverLetterBinding::inflate

    override fun observeLiveData() {


    }

    @SuppressLint("ResourceType")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.apply {
            textView.text="Add Detail"
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.coverletterTextInputLayout.apply {
            // Adjust the layout parameters of the end icon
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }

        // Extension function to convert dp to pixels

        binding.selectSamplebtn.setOnClickListener {
              currentActivity().replaceChoiceFragment(R.id.nav_sample_cover_letter)
        }
        binding.nextbtn.setOnClickListener {
            if (!binding.coverletterTextInput.text.isNullOrEmpty())
            {
                currentActivity().replaceChoiceFragment(R.id.nav_add_preview_resume)
            }else{
                currentActivity().showToast("Please write CoverLetter")
            }
        }
        binding.coverletterTextInputLayout.setEndIconOnClickListener{
            currentActivity().replaceChoiceFragment(R.id.nav_ai_writing_coverletter_sample)
        }


    }

    override fun onResume() {
        super.onResume()
        val coverletter=arguments?.getString(Constants.DATA)
        if (!coverletter.isNullOrEmpty())
        {
            binding.coverletterTextInput.setText(coverletter.toString())
        }
    }

}
