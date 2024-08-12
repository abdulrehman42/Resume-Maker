package com.example.resumemaker.views.fragments.choose.coverletter

import android.os.Bundle
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddDetailCoverLetterBinding

class AddDetailCoverLetterFragment : BaseFragment<FragmentAddDetailCoverLetterBinding>(){
    override val inflate: Inflate<FragmentAddDetailCoverLetterBinding>
        get() = FragmentAddDetailCoverLetterBinding::inflate

    override fun observeLiveData() {


    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.apply {
            textView.text="Add Detail"
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.selectSamplebtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_sample_cover_letter)
        }



    }

}
