package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Qualification
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEducation(
    val data: ProfileModelAddDetailResponse.UserQualification?,
    val userQualificationsList: List<ProfileModelAddDetailResponse.UserQualification>?,
    val isEdit: Boolean

) :
    AddDetailsBaseFragment<FragmentAddEducationBinding>() {
    private var isProgrammaticallySettingText = false
    private var isDegree = false
    var withWord = "-1__"
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserQualification>()
    var updatedList = ArrayList<Qualification>()
    private var looksAdapter = LooksAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var endDate: String? = null

    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.educationResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()

        }
        addDetailResumeVM.looksupResponse.observe(this) {
            looksAdapter.submitList(it)
            if (it.size == 0) {
                binding.lookidRecyclerview.isGone = true
                binding.lookidRecyclerview.isGone = true
            } else {
                if (isDegree) {
                    binding.lookidRecyclerviewdegree.isGone = false
                } else {
                    binding.lookidRecyclerview.isGone = false
                }

            }
        }

        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
                AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
                if (it.msg.isNotBlank()) {
                    AppsKitSDKUtils.makeToast(it.msg)
                }
            }
        }
    }


    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_education)
        AppsKitSDKAdsManager.showNative(
            currentActivity(), binding.bannerAdd, ""

        )
        userQualificationsList?.let {
            oldList =
                userQualificationsList as ArrayList<ProfileModelAddDetailResponse.UserQualification>
        }
        data?.let {
            binding.instituenameedittext.setText(Helper.removeOneUnderscores(data.institute))
            binding.degreeName.setText(Helper.removeOneUnderscores(data.degree))
            binding.startdateedittext.setText(
                Helper.convertIsoToCustomFormat(data.startDate)
            )
            binding.enddateedittext.setText(Helper.convertIsoToCustomFormat(data.endDate))
        }
        onclick()
        if (isDegree) {
            binding.lookidRecyclerviewdegree.adapter = looksAdapter
        } else {
            binding.lookidRecyclerviewdegree.adapter = looksAdapter

        }
    }

    private fun onclick() {
        binding.degreeName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApi(null) // Send null query if the text is erased
                        binding.lookidRecyclerviewdegree.isGone = true
                    } else {
                        callLookUpApi(query)
                        binding.lookidRecyclerviewdegree.isGone = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiInstitute(null) // Send null query if the text is erased
                        binding.lookidRecyclerviewdegree.isGone = true
                    } else {
                        callLookUpApi(query)
                        binding.lookidRecyclerviewdegree.isGone = false
                    }
                }
                isProgrammaticallySettingText = false

            }
        })
        binding.instituenameedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApi(null) // Send null query if the text is erased
                        binding.lookidRecyclerview.isGone = true
                    } else {
                        callLookUpApi(query)
                        binding.lookidRecyclerview.isGone = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiInstitute(null) // Send null query if the text is erased
                        binding.lookidRecyclerview.isGone = true
                    } else {
                        callLookUpApiInstitute(query)
                        binding.lookidRecyclerview.isGone = false
                    }
                }
                isProgrammaticallySettingText = false

            }
        })



        binding.checkItscontinue.setOnClickListener {
            if (binding.checkItscontinue.isChecked) {
                binding.enddateTextInputLayout2.isEnabled = false

            } else {
                binding.enddateTextInputLayout2.isEnabled = true
            }
        }
        looksAdapter.setOnItemClickCallback {
            if (isDegree) {
                withWord = "1__"
                binding.degreeName.setText(Helper.removeOneUnderscores(it.text))
                binding.lookidRecyclerviewdegree.isGone = true
            } else {
                binding.instituenameedittext.setText(Helper.removeOneUnderscores(it.text))
                binding.lookidRecyclerview.isGone = true
            }
            isProgrammaticallySettingText = true
        }
        binding.startdateedittext.setOnClickListener {
            DialogueBoxes.showWheelDatePickerDialog(
                currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.startdateedittext.setText(date)
                    }
                }
            )
        }
        binding.enddateedittext.setOnClickListener {
            DialogueBoxes.showWheelDatePickerDialog(
                currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.enddateedittext.setText(date)
                    }
                }
            )
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
        currentActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun CallApi() {
        // Determine the end date based on the checkbox state
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }

        // Update the list with existing qualifications
        updateQualificationList()

        // Add or update the qualification based on edit mode
        val newQualification = Qualification(
            degree = withWord + binding.degreeName.text.toString(),
            endDate = endDate,
            institute = withWord + binding.instituenameedittext.text.toString(),
            qualificationType = "degree",
            startDate = binding.startdateedittext.text.toString()
        )

        // Add or replace the qualification in the list
        if (!isEdit) {
            updatedList.add(newQualification)
        } else {
            replaceQualificationInList(newQualification)
        }

        // Create and send the API request
        val qualificationModelRequest = QualificationModelRequest(qualifications = updatedList)
        addDetailResumeVM.editQualification(qualificationModelRequest)
    }


    fun isConditionMet(): Boolean {
        val isEndDateRequired = binding.checkItscontinue.isChecked
        return !binding.instituenameedittext.text.toString().isNullOrEmpty() &&
                !binding.degreeName.text.toString().isNullOrEmpty() &&
                !binding.startdateedittext.text.toString().isNullOrEmpty() &&
                (!isEndDateRequired || !binding.enddateedittext.text.toString().isNullOrEmpty())
    }

    private fun updateQualificationList() {
        // Initialize the updated list with existing qualifications
        updatedList.clear()
        oldList.forEach { oldQualification ->
            updatedList.add(
                Qualification(
                    degree = oldQualification.degree,
                    endDate = oldQualification.endDate,
                    institute = oldQualification.institute,
                    qualificationType = oldQualification.qualificationType,
                    startDate = oldQualification.startDate
                )
            )
        }
    }

    private fun replaceQualificationInList(newQualification: Qualification) {
        val indexToUpdate = updatedList.indexOfFirst {
            it.degree == newQualification.degree ||
                    it.institute == newQualification.institute ||
                    it.startDate == newQualification.startDate ||
                    it.endDate == newQualification.endDate ||
                    it.qualificationType == newQualification.qualificationType
        }

        if (indexToUpdate != -1) {
            updatedList[indexToUpdate] = newQualification
        } else {
            updatedList.add(newQualification)
        }
    }

    private fun callLookUpApi(query: String?) {
        isDegree = true
        addDetailResumeVM.getLookUp(Constants.degree, query, "", "6")
    }

    private fun callLookUpApiInstitute(query: String?) {
        isDegree = false
        addDetailResumeVM.getLookUp(Constants.institute, query, "", "6")
    }
}