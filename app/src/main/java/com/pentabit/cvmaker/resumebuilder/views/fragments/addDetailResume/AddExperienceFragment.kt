package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider

import com.pentabit.cvmaker.resumebuilder.utils.Helper.dpToPx
import com.google.android.material.textfield.TextInputLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddExperienceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Experience
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
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
class AddExperienceFragment(
    val data: ProfileModelAddDetailResponse.UserExperience?,
    val experienceList: ArrayList<ProfileModelAddDetailResponse.UserExperience>?,
    val isEdit: Boolean
) :
    BaseFragment<FragmentAddExperienceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var endDate: String? = null
    var withWord = "-1__"

    private var looksAdapter = LooksAdapter()
    var isCompany = false
    var isProgrammaticallySettingText = false
    var isProgrammaticallySettingText1 = false

    var oldList = ArrayList<ProfileModelAddDetailResponse.UserExperience>()
    val updateList = ArrayList<Experience>()
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.experienceResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }

        }
        addDetailResumeVM.looksupResponse.observe(this) {
            looksAdapter.submitList(it)
            if (it.size == 0) {
                binding.lookidRecyclerview.isGone = true
                binding.lookidRecyclerviewcompany.isGone = true
            } else {
                if (isCompany) {
                    binding.lookidRecyclerviewcompany.isGone = false
                } else {
                    binding.lookidRecyclerview.isGone = false
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]

        binding.includeTool.textView.text = getString(R.string.add_experience)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        experienceList?.let {
            oldList = experienceList
            for (i in 0 until oldList.size) {
                updateList.add(
                    Experience(
                        oldList[i].company,
                        oldList[i].description,
                        oldList[i].employmentType,
                        oldList[i].endDate,
                        oldList[i].startDate,
                        oldList[i].title
                    )
                )
            }
        }

        data?.let {
            binding.jobName.setText(Helper.removeOneUnderscores(data.title))
            binding.description.setText(data.description)
            binding.companyName.setText(Helper.removeOneUnderscores(data.company))
            binding.startdateedittext.setText(
                Helper.convertIsoToCustomFormat(data.startDate)
            )
            binding.enddateedittext.setText(
                Helper.convertIsoToCustomFormat(
                    data.endDate
                )
            )
        }

        binding.descriptionTextInputLayout2.apply {
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView =
                findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }
        if (isCompany) {
            binding.lookidRecyclerviewcompany.adapter = looksAdapter
        } else {
            binding.lookidRecyclerview.adapter = looksAdapter

        }
        onclick()

    }

    private fun onclick() {
        binding.companyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText1) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiCompany(null.toString()) // Send null query if the text is erased
                        binding.lookidRecyclerviewcompany.isGone = true
                    } else {
                        callLookUpApiCompany(query)
                        binding.lookidRecyclerviewcompany.isGone = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticallySettingText1) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiCompany(null.toString()) // Send null query if the text is erased
                        binding.lookidRecyclerviewcompany.isGone = true
                    } else {
                        callLookUpApiCompany(query)
                        binding.lookidRecyclerviewcompany.isGone = false
                    }
                }
                isProgrammaticallySettingText = false

            }
        })
        binding.jobName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiJob(null) // Send null query if the text is erased
                        binding.lookidRecyclerview.isGone = true
                    } else {
                        callLookUpApiJob(query)
                        binding.lookidRecyclerview.isGone = false
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        callLookUpApiJob(null) // Send null query if the text is erased
                        binding.lookidRecyclerview.isGone = true
                    } else {
                        callLookUpApiJob(query)
                        binding.lookidRecyclerview.isGone = false
                    }
                }
                isProgrammaticallySettingText = false

            }
        })



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
        binding.checkItscontinue.setOnClickListener {
            if (binding.checkItscontinue.isChecked) {
                binding.enddateTextInputLayout2.isEnabled = false
                endDate = null
            } else {
                binding.enddateTextInputLayout2.isEnabled = true
            }
        }
        binding.savebtn.setOnClickListener {

            if (Validations.isConditionMetExperience(binding)) {
                apiCall()
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
        looksAdapter.setOnItemClickCallback {
            withWord = "1__"
            if (isCompany) {
                isProgrammaticallySettingText1 = true

                binding.companyName.setText(Helper.removeOneUnderscores(it.text))
                binding.lookidRecyclerviewcompany.isGone = true
            } else {
                isProgrammaticallySettingText = true

                binding.jobName.setText(Helper.removeOneUnderscores(it.text))
                binding.lookidRecyclerview.isGone = true
            }
        }

    }

    private fun callLookUpApiCompany(query: String?) {
        isCompany = true
        addDetailResumeVM.getLookUp(Constants.company, query, "", "6")

    }

    private fun callLookUpApiJob(query: String?) {
        isCompany = false
        addDetailResumeVM.getLookUp(Constants.jobTitle, query, "", "6")

    }


    private fun apiCall() {
        updateList()
        val experience =
            Experience(
                withWord + binding.companyName.text.toString(),
                binding.description.text.toString(),
                employmentType = "fullTime", endDate, binding.startdateedittext.text.toString(),
                withWord + binding.jobName.text.toString()
            )
        if (!isEdit) {
            updateList.add(experience)
        } else {
            replaceExperienceInList(experience)
        }
        /*updateList.add(
            Experience(
                "-1__" + binding.companyName.text.toString(),
                binding.description.text.toString(),
                employmentType = "fullTime", endDate, binding.startdateedittext.text.toString(),
                "-1__" + binding.jobName.text.toString()
            )
        )*/
        val experienceRequest = ExperienceRequest(experiences = updateList)
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            binding.enddateedittext.text.toString()
        }
        addDetailResumeVM.editExperience(
            experienceRequest
        )

    }

    private fun replaceExperienceInList(experience: Experience) {
        val indexToUpdate = updateList.indexOfFirst {
            it.title == experience.title ||
                    it.company == experience.company ||
                    it.startDate == experience.startDate ||
                    it.endDate == experience.endDate ||
                    it.description == experience.description
        }
        if (indexToUpdate != -1) {
            updateList[indexToUpdate] = experience
        } else {
            updateList.add(experience)
        }
    }

    private fun updateList() {
        updateList.clear()
        oldList.forEach { old ->
            updateList.add(
                Experience(
                    company = old.company,
                    description = old.description,
                    employmentType = old.employmentType,
                    endDate = old.endDate,
                    startDate = old.startDate,
                    title = old.title
                )
            )
        }
    }

}