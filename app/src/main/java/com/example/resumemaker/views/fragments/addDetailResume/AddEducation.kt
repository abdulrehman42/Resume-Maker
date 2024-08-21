package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddEducationBinding
import com.example.resumemaker.models.request.addDetailResume.Qualification
import com.example.resumemaker.models.request.addDetailResume.QualificationModelRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEducation : AddDetailsBaseFragment<FragmentAddEducationBinding>() {
    val addDetailResumeVM by viewModels<AddDetailResumeVM>()
    var endDate: String? = null

    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_education)
        val data = sharePref.readDataEducation()
        if (data.degree != null) {
            binding.instituenameedittext.setText(data.institute)
            binding.degreeName.setText(data.degree)
            binding.startdateedittext.setText(data.startDate)
            binding.enddateedittext.setText(data.endDate)
        }
        onclick()

    }

    private fun onclick() {
        binding.checkItscontinue.setOnClickListener {
            if (binding.checkItscontinue.isChecked) {
                binding.enddateTextInputLayout2.isEnabled = false

            } else {
                binding.enddateTextInputLayout2.isEnabled = true
            }
        }

        binding.startdateedittext.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    // The EditText gained focus
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                // Handle the result here
                                binding.startdateedittext.setText(date)
                            }
                        })
                }
            }
        binding.enddateedittext.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    // The EditText gained focus
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                // Handle the result here
                                binding.enddateedittext.setText(date)
                            }
                        })
                }
            }


        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                CallApi()
            } else {
                currentActivity().showToast(getString(R.string.field_missing_error))
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = true
            requireActivity().onBackPressedDispatcher.onBackPressed()

        }
        requireActivity().onBackPressedDispatcher.addCallback(currentActivity()) {
            addDetailResumeVM.isHide.value = true
        }
    }

    private fun CallApi() {
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }
        val qualifications = listOf(
            Qualification(
                "1__"+binding.degreeName.text.toString(),
                endDate,
                "1__"+binding.instituenameedittext.text.toString(),
                "degree",
                binding.startdateedittext.text.toString()
            )
        )
        val qualificationModelRequest = QualificationModelRequest(qualifications = qualifications)

        addDetailResumeVM.editQualification(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            qualificationModelRequest
        )
    }


    fun isConditionMet(): Boolean {
        return !binding.instituenameedittext.text.toString().isNullOrEmpty() &&
                !binding.degreeName.text.toString().isNullOrEmpty() &&
                !binding.startdateedittext.text.toString().isNullOrEmpty()
    }
}