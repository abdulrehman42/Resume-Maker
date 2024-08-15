package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddExperienceBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper.dpToPx
import com.google.android.material.textfield.TextInputLayout
import com.ozcanalasalvar.datepicker.utils.DateUtils.getCurrentTime
import com.ozcanalasalvar.datepicker.view.datepicker.DatePicker

class AddExperienceFragment : BaseFragment<FragmentAddExperienceBinding>(){
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Experience"
        val data=sharePref.readDataEducation()
        if (data!=null)
        {
            binding.jobName.setText(data.universityName)
            binding.description.setText(data.degree)
            binding.companyName.setText(data.universityName)
            binding.startdateedittext.setText(data.startDate)
            binding.enddateedittext.setText(data.endDate)
        }


        binding.descriptionTextInputLayout2.apply {
            // Adjust the layout parameters of the end icon
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }
        onclick()

    }

    private fun onclick() {
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