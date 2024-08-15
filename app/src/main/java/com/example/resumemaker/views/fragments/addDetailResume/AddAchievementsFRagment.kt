package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import android.widget.Toast
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddAchievementsFRagmentBinding
import com.example.resumemaker.utils.DialogueBoxes


class AddAchievementsFRagment : BaseFragment<FragmentAddAchievementsFRagmentBinding>()
{
    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Achievement"
        val data=sharePref.readDataEducation()
        if (data!=null)
        {
            binding.achieveedittext.setText(data.universityName)
            binding.descriptionedittext.setText(data.degree)
            binding.issueDateeedittext.setText(data.startDate+data.endDate)
        }
        onclick()

    }



    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.issueDateeedittext.onFocusChangeListener =
            OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    // The EditText gained focus
                    DialogueBoxes.showWheelDatePickerDialog(currentActivity(), object : DialogueBoxes.StringDialogCallback {
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


}