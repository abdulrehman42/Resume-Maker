package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.EducationResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Qualification
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEducation(val data: ProfileModelAddDetailResponse.UserQualification?) :
    AddDetailsBaseFragment<FragmentAddEducationBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var endDate: String? = null

    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.educationResponse.observe(this) {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {

            if (it.loader) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
            if (!it.msg.isNullOrBlank()) {
                currentActivity().showToast(it.msg)
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_education)
        AppsKitSDKAdsManager.showNative(
            currentActivity(), binding.bannerAdd, ""

        );
//        val data = sharePref.readDataEducation()
        data?.let {
            binding.instituenameedittext.setText(data.institute)
            binding.degreeName.setText(data.degree)
            binding.startdateedittext.setText(
                Helper.convertIsoToCustomFormat(data.startDate)
            )
            binding.enddateedittext.setText(Helper.convertIsoToCustomFormat(data.endDate))
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
                    com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object :
                            com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringDialogCallback {
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
                    com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object :
                            com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringDialogCallback {
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
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        /*currentActivity().onBackPressedDispatcher.addCallback (viewLifecycleOwner){
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }*/
    }

    private fun CallApi() {
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }

        val qualifications = listOf(
            Qualification(
                degree = "-1__" + binding.degreeName.text.toString(),
                endDate = endDate,
                institute = "-1__" + binding.instituenameedittext.text.toString(),
                qualificationType = "degree",
                startDate = binding.startdateedittext.text.toString()
            )
        )

        val qualificationModelRequest = QualificationModelRequest(qualifications = qualifications)

        addDetailResumeVM.editQualification(
            qualificationModelRequest
        )
    }


    fun isConditionMet(): Boolean {
        return !binding.instituenameedittext.text.toString().isNullOrEmpty() &&
                !binding.degreeName.text.toString().isNullOrEmpty() &&
                !binding.startdateedittext.text.toString().isNullOrEmpty()
    }
}