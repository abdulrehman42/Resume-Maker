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
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExperienceFragment(
    val experienceList: ArrayList<ProfileModelAddDetailResponse.UserExperience>,
    val position: Int?,
    val callback: OnExperienceUpdate
) :
    BaseFragment<FragmentAddExperienceBinding>() {

    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val screenId = ScreenIDs.ADD_EXPERIENCE
    private lateinit var companyPredictiveSearchHandler: PredictiveSearchHandler
    private lateinit var titlePredictiveSearchHandler: PredictiveSearchHandler
    private var isCompanyInDB = false
    private var isTileInDB = false
    var endDate: String? = null


    override val inflate: Inflate<FragmentAddExperienceBinding>
        get() = FragmentAddExperienceBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_experience)
        populateDataIfRequired()
        managePredictiveSearch()
        handleUI()
        handleClicks()
    }


    override fun observeLiveData() {
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
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
        // TODO Need to Edit
        if (position != null) {
            val model = experienceList[position]
            isCompanyInDB = model.company.startsWith("1_")
            isTileInDB = model.title.startsWith("1_")

            binding.jobName.setText(Helper.removeOneUnderscores(model.title))
            binding.companyName.setText(Helper.removeOneUnderscores(model.company))
            binding.startdateedittext.setText(
                Helper.convertIsoToCustomFormat(model.startDate)
            )
            binding.description.setText(Helper.removeOneUnderscores(model.description))
            binding.enddateedittext.setText(Helper.convertIsoToCustomFormat(model.endDate))
            binding.checkItscontinue.isChecked = model.endDate.isNullOrEmpty()
            binding.enddateTextInputLayout2.isEnabled = !binding.checkItscontinue.isChecked
        }
    }

    private fun managePredictiveSearch() {
        // TODO Need to edit
        companyPredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.company,
            isAlreadyInDB = isCompanyInDB,
            autoCompleteTextView = binding.companyName,
            viewModel = addDetailResumeVM
        )
        titlePredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.jobTitle,
            isAlreadyInDB = isTileInDB,
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
                binding.enddateTextInputLayout2.isEnabled = !binding.checkItscontinue.isChecked
                if (binding.checkItscontinue.isChecked) {
                    binding.enddateedittext.setText("")
                }
            }
        }
        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetExperience(binding)) {
                saveExperience()
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun saveExperience() {
        //updateList()
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            Helper.convertToUTCTimeForma(binding.enddateedittext.text.toString().trim())
        }

        val experienceupdate = ProfileModelAddDetailResponse.UserExperience(
            companyPredictiveSearchHandler.getText(),
            binding.description.text.toString(),
            "fullTime",
            endDate = endDate,
            Helper.convertToUTCTimeForma(binding.startdateedittext.text.toString()),
            titlePredictiveSearchHandler.getText(),
        )

        val updatedListExperience =
            ArrayList<ProfileModelAddDetailResponse.UserExperience>(experienceList)

        if (position != null) {
            updatedListExperience[position] = experienceupdate

        } else {
            updatedListExperience.add(experienceupdate)
        }
        callback.onExperience(updatedListExperience)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }


    interface OnExperienceUpdate {
        fun onExperience(experiencelist: ArrayList<ProfileModelAddDetailResponse.UserExperience>?)
    }

}