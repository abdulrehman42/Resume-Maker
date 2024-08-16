package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddReferenceBinding

class AddReferenceFragment : BaseFragment<FragmentAddReferenceBinding>() {
    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun observeLiveData() {
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = "Add Reference"
        val data = sharePref.readDataEducation()
        if (data != null) {
            binding.referrencenameedit.setText(data.degree)
            binding.companyName.setText(data.universityName)
            binding.emailedit.setText(data.startDate)
            binding.phone.setText(data.endDate)
        }
        onclick()
    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            requireActivity().finish()

//            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.savebtn.setOnClickListener {
            if (isConditionMet())
            {
                requireActivity().finish()

            }

//            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    fun isConditionMet(): Boolean {
        return !binding.jobedittext.text.toString().isNullOrEmpty() &&
                !binding.referrencenameedit.text.toString().isNullOrEmpty()&&
                !binding.emailedit.text.toString().isNullOrEmpty() &&
                !binding.phone.text.toString().isNullOrEmpty() &&
                !binding.companyName.text.toString().isNullOrEmpty()
    }
}