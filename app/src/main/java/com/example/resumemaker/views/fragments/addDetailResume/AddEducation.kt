package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddEducationBinding
import com.example.resumemaker.utils.DialogueBoxes

class AddEducation : BaseFragment<FragmentAddEducationBinding>() {
    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun observeLiveData() {
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = "Add Education"
        val data = sharePref.readDataEducation()
        if (data != null) {
            binding.instituenameedittext.setText(data.universityName)
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
                    currentActivity().onBackPressedDispatcher.onBackPressed()
                }
                binding.includeTool.backbtn.setOnClickListener {
                    currentActivity().onBackPressedDispatcher.onBackPressed()

                }
            }

    }
}