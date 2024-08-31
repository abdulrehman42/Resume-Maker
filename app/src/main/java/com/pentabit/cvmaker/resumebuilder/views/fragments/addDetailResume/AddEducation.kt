package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Qualification
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEducation(
    val data: ProfileModelAddDetailResponse.UserQualification?,
    val userQualificationsList: List<ProfileModelAddDetailResponse.UserQualification>?,
    val isEdit: Boolean,
    val position: Int

) :
    BaseFragment<FragmentAddEducationBinding>() {
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
        }

        /*addDetailResumeVM.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }

        }*/
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
            if (data.endDate.isEmpty())
            {
                binding.checkItscontinue.isChecked=true
            }
        }
        onclick()
        if (isDegree) {
            binding.lookidRecyclerviewdegree.adapter = looksAdapter
        } else {
            binding.lookidRecyclerviewdegree.adapter = looksAdapter

        }
    }

    private fun onclick() {
        setupTextWatcher(binding.degreeName, isDegree = true)
        setupTextWatcher(binding.instituenameedittext, isDegree = false)
        binding.checkItscontinue.setOnClickListener {
            if (binding.checkItscontinue.isChecked) {
                binding.enddateTextInputLayout2.isEnabled = false
                binding.enddateedittext.setText("")

            } else {
                binding.enddateTextInputLayout2.isEnabled = true
            }
        }
        looksAdapter.setOnItemClickCallback {
            isProgrammaticallySettingText = true
            val text = Helper.removeOneUnderscores(it.text)
            withWord = "-1__"
            if (isDegree) {
                binding.degreeName.setText(text)
                binding.lookidRecyclerviewdegree.isGone = true
            } else {
                binding.instituenameedittext.setText(text)
                binding.lookidRecyclerview.isGone = true
            }
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
            if (Validations.isConditionMetEducation(binding)) {
                CallApi()
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

        currentActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun setupTextWatcher(editText: EditText, isDegree: Boolean) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        handleEmptyQuery(isDegree)
                    } else {
                        handleNonEmptyQuery(query, isDegree)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isProgrammaticallySettingText = false
            }
        })
    }

    private fun handleEmptyQuery(isDegree: Boolean) {
        if (isDegree) {
            binding.lookidRecyclerviewdegree.isGone = true
        } else {
            binding.lookidRecyclerview.isGone = true
        }
        callLookUpApi(null, isDegree)
    }

    private fun handleNonEmptyQuery(query: String, isDegree: Boolean) {
        callLookUpApi(query, isDegree)
        if (isDegree) {
            binding.lookidRecyclerviewdegree.isGone = false
        } else {
            binding.lookidRecyclerview.isGone = false
        }
    }

    private fun callLookUpApi(query: String?, isDegree: Boolean) {
        this.isDegree = isDegree
        val type = if (isDegree) Constants.degree else Constants.institute
        addDetailResumeVM.getLookUp(type, query, "", "6")

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
            degree = withWord + Helper.removeOneUnderscores(binding.degreeName.text.toString()),
            endDate = endDate,
            institute = withWord + Helper.removeOneUnderscores(binding.instituenameedittext.text.toString()),
            qualificationType = "degree",
            startDate = binding.startdateedittext.text.toString()
        )

        // Add or replace the qualification in the list
        if (!isEdit) {
            updatedList.add(newQualification)
        } else {
            updatedList[position] = newQualification
            //replaceQualificationInList(newQualification)
        }

        // Create and send the API request
        val qualificationModelRequest = QualificationModelRequest(qualifications = updatedList)
        addDetailResumeVM.editQualification(qualificationModelRequest)
    }


    private fun updateQualificationList() {
        // Initialize the updated list with existing qualifications
        //updatedList.clear()
        oldList.forEach { oldQualification ->
            updatedList.add(
                Qualification(
                    degree = withWord + Helper.removeOneUnderscores(oldQualification.degree),
                    endDate = oldQualification.endDate,
                    institute = withWord + Helper.removeOneUnderscores(oldQualification.institute),
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

}