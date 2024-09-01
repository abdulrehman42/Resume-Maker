package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
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
import com.pentabit.cvmaker.resumebuilder.utils.Helper.dpToPx
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExperienceFragment(
    val data: ProfileModelAddDetailResponse.UserExperience?,
    val experienceList: ArrayList<ProfileModelAddDetailResponse.UserExperience>?,
    val isEdit: Boolean,
    val position: Int
) :
    BaseFragment<FragmentAddExperienceBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var companyPredictiveSearchHandler: PredictiveSearchHandler
    private lateinit var titlePredictiveSearchHandler: PredictiveSearchHandler
    var endDate: String? = null
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserExperience>()
    val updateList = ArrayList<Experience>()
    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.experienceResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_experience)
        populateDataIfRequired()
        managePredictiveSearch()
        handleUI()
        handleClicks()
    }

    private fun handleUI() {
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
    }

    private fun populateDataIfRequired() {
        experienceList?.let {
            oldList = experienceList
            for (i in 0 until oldList.size) {
                updateList.add(
                    Experience(
                        "1__" + Helper.removeOneUnderscores(oldList[i].company),
                        Helper.removeOneUnderscores(oldList[i].description),
                        Helper.removeOneUnderscores(oldList[i].employmentType),
                        Helper.convertIsoToCustomFormat(oldList[i].endDate),
                        Helper.convertIsoToCustomFormat(oldList[i].startDate),
                        "1__" + Helper.removeOneUnderscores(oldList[i].title)
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
            if (data.endDate.isEmpty()) {
                binding.checkItscontinue.isChecked = true
            }
        }
    }

    private fun managePredictiveSearch() {
        companyPredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.company,
            isAlreadyInDB = isEdit,
            autoCompleteTextView = binding.companyName,
            viewModel = addDetailResumeVM
        )
        titlePredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.jobTitle,
            isAlreadyInDB = isEdit,
            autoCompleteTextView = binding.jobName,
            viewModel = addDetailResumeVM
        )
    }

    private fun handleClicks() {
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
                binding.enddateedittext.setText("")
                endDate = null
            } else {
                binding.enddateTextInputLayout2.isEnabled = true
            }
        }
        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetExperience(binding)) {
                saveExperience()
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressed()
        }
    }


    private fun saveExperience() {
        updateList()
        val experience =
            Experience(
                companyPredictiveSearchHandler.getText(),
                binding.description.text.toString(),
                employmentType = "fullTime",
                endDate, binding.startdateedittext.text.toString(),
                titlePredictiveSearchHandler.getText()
            )
        if (!isEdit) {
            updateList.add(experience)
        } else {
            updateList[position] = experience
        }
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