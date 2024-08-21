package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddEducationBinding
import com.example.resumemaker.models.request.addDetailResume.QualificationRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEducation : BaseFragment<FragmentAddEducationBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var endDate: String? =null


    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner){
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_education)
        val data = sharePref.readDataEducation()
        if (data.degree!=null) {
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

    private fun CallApi() {
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }
        addDetailResumeVM.editQualification(sharePref.readString(Constants.PROFILE_ID).toString(),
            QualificationRequestModel("1__"+binding.degreeName.text.toString(),endDate,"1__"+binding.instituenameedittext.text.toString(),"degree",binding.startdateedittext.text.toString())
        )
    }


    fun isConditionMet(): Boolean {
        return !binding.instituenameedittext.text.toString().isNullOrEmpty()&&
                !binding.degreeName.text.toString().isNullOrEmpty()&&
                !binding.startdateedittext.text.toString().isNullOrEmpty()
    }
}