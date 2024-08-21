package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddExperienceBinding
import com.example.resumemaker.models.request.addDetailResume.AchievRequestModel
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExperienceFragment : BaseFragment<FragmentAddExperienceBinding>(){
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text=getString(R.string.add_experience)
        addDetailResumeVM= ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]

        val data=sharePref.readProfileExperience()
        if (data!=null)
        {
            binding.jobName.setText(data.title)
            binding.description.setText(data.description)
            binding.companyName.setText(data.company)
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
            if (isConditionMet()) {
                apiCall()
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
        return !binding.companyName.text.toString().isNullOrEmpty()&&
                !binding.jobName.text.toString().isNullOrEmpty()&&
                !binding.description.text.toString().trim().isNullOrEmpty()&&
                !binding.startdateedittext.text.toString().isNullOrEmpty()&&
                !binding.enddateedittext.text.toString().isNullOrEmpty()
    }

    private fun apiCall() {
        addDetailResumeVM.editExperience(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            ExperienceRequestModel(
                binding.companyName.text.toString(),
                binding.description.text.toString(),
                employmentType = "",binding.enddateedittext.text.toString(),binding.startdateedittext.text.toString(),
                binding.jobName.text.toString()
            )
        )
    }

}