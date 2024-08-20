package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddReferenceBinding
import com.example.resumemaker.models.request.addDetailResume.ProjectRequestModel
import com.example.resumemaker.models.request.addDetailResume.ReferenceRequestModel
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReferenceFragment : BaseFragment<FragmentAddReferenceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_referrence)
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
        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                addDetailResumeVM.isHide.value=true
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value=true
        }
    }

    fun isConditionMet(): Boolean {
        return !binding.jobedittext.text.toString().isNullOrEmpty() &&
                !binding.referrencenameedit.text.toString().isNullOrEmpty()&&
                !binding.emailedit.text.toString().isNullOrEmpty() &&
                !binding.phone.text.toString().isNullOrEmpty() &&
                !binding.companyName.text.toString().isNullOrEmpty()
    }

    private fun apiCall() {
        addDetailResumeVM.editReference(
            "7422", ReferenceRequestModel(binding.companyName.text.toString(),binding.emailedit.text.toString()
            ,binding.referrencenameedit.text.toString(),binding.phone.text.toString(),binding.jobedittext.text.toString())
        )
    }
}