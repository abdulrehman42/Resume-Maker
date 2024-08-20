package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddAchievementsFRagmentBinding
import com.example.resumemaker.models.request.addDetailResume.AchievRequestModel
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAchievementsFRagment : BaseFragment<FragmentAddAchievementsFRagmentBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]

        binding.includeTool.textView.text = getString(R.string.add_achievement)
        val data = sharePref.readDataEducation()
        if (data.degree!=null) {
            binding.achieveedittext.setText(data.universityName)
            binding.descriptionedittext.setText(data.degree)
            binding.issueDateeedittext.setText(data.startDate + data.endDate)
        }
        onclick()

    }
    private fun onclick() {
        binding.savebtn.setOnClickListener {
            apiCall()
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
        }
        binding.issueDateeedittext.onFocusChangeListener =
            OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    // The EditText gained focus
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                // Handle the result here
                                binding.issueDateeedittext.setText(date)
                            }
                        })
                }
            }
        binding.issueDateeedittext.setOnClickListener {

        }
    }

    private fun apiCall() {
        addDetailResumeVM.editAchievement(
            "7422",
            AchievRequestModel(
                binding.descriptionedittext.text.toString(),
                binding.issueDateeedittext.text.toString(),
                binding.achieveedittext.text.toString()
            )
        )
    }


}