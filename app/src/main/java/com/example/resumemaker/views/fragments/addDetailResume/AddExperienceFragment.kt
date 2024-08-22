package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddExperienceBinding
import com.example.resumemaker.models.api.adddetailresume.ExperienceResponse
import com.example.resumemaker.models.request.addDetailResume.Experience
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper.dpToPx
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddExperienceFragment : BaseFragment<FragmentAddExperienceBinding>(){
    lateinit var addDetailResumeVM :AddDetailResumeVM
    var endDate: String? =null
    val list=ArrayList<ExperienceResponse>()
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.educationResponse.observe(currentActivity()) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]

        binding.includeTool.textView.text=getString(R.string.add_experience)

        val data=sharePref.readProfileExperience()
        data?.let {
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
                endDate=null
            }else
            {
                binding.enddateTextInputLayout2.isEnabled=true
            }
        }
        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                apiCall()
                MainScope().launch {
                    delay(2000)
                    addDetailResumeVM.isHide.value = true
                    currentActivity().onBackPressedDispatcher.onBackPressed()
                }
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        /*currentActivity().onBackPressedDispatcher.addCallback(this) {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }*/
    }
    fun isConditionMet(): Boolean {
        return !binding.companyName.text.toString().isNullOrEmpty()&&
                !binding.jobName.text.toString().isNullOrEmpty()&&
                !binding.description.text.toString().trim().isNullOrEmpty()&&
                !binding.startdateedittext.text.toString().isNullOrEmpty()}

    private fun apiCall() {

        val experience = listOf(
            Experience(
                "1__"+binding.companyName.text.toString(),
                binding.description.text.toString(),
                employmentType = "fullTime",endDate,binding.startdateedittext.text.toString(),
                "1__"+binding.jobName.text.toString()
            )
        )
        val experienceRequest = ExperienceRequest(experiences = experience)



        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }
        addDetailResumeVM.editExperience(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            experienceRequest
        )
    }

}