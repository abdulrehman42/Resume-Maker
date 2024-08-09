package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddExperienceBinding
import com.example.resumemaker.databinding.FragmentAddSkillBinding
import com.example.resumemaker.databinding.FragmentExperienceBinding

class AddExperienceFragment : BaseFragment<FragmentAddExperienceBinding>(){
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Experience"
        onclick()

    }

    private fun onclick() {
        binding.checkItscontinue.setOnClickListener {
            if (binding.checkItscontinue.isChecked)
            {
                binding.enddateTextInputLayout2.isEnabled=false
            }else
            {
                binding.enddateTextInputLayout2.isEnabled=true
            }
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
    }

}