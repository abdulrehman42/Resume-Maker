package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnEducationUpdate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddEducation(
    val userQualificationsList: List<ProfileModelAddDetailResponse.UserQualification>,
    val position: Int?,
    val callback: OnEducationUpdate
) : BaseFragment<FragmentAddEducationBinding>() {

    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val screenId=ScreenIDs.ADD_EDUCATION
    var endDate: String? = null
    private lateinit var degreePredictiveSearchHandler: PredictiveSearchHandler
    private lateinit var institutePredictiveSearchHandler: PredictiveSearchHandler
    private var isDegreeInDB = false
    private var isInstituteInDB = false

    override val inflate: Inflate<FragmentAddEducationBinding>
        get() = FragmentAddEducationBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_education)
        manageAds()
        populateInfoIfRequired()
        handleClicks()
        managePredictiveSearchAdapter()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }

    override fun observeLiveData() {
        // not required
    }

    private fun populateInfoIfRequired() {
        if (position != null) {
            val model = userQualificationsList[position]
            isInstituteInDB = model.institute.startsWith("1_")
            isDegreeInDB = model.degree.startsWith("1_")

            binding.instituenameedittext.setText(Helper.removeOneUnderscores(model.institute))
            binding.degreeName.setText(Helper.removeOneUnderscores(model.degree))
            binding.startdateedittext.setText(
                Helper.convertIsoToCustomFormat(model.startDate)
            )
            binding.checkItscontinue.isChecked = model.endDate.isNullOrEmpty()
            binding.enddateTextInputLayout2.isEnabled = !binding.checkItscontinue.isChecked
            binding.enddateedittext.setText(Helper.convertIsoToCustomFormat(model.endDate))
        }
    }

    private fun managePredictiveSearchAdapter() {
        degreePredictiveSearchHandler =
            PredictiveSearchHandler(
                key = Constants.degree,
                isAlreadyInDB = isDegreeInDB,
                autoCompleteTextView = binding.degreeName,
                viewModel = addDetailResumeVM
            )
        institutePredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.institute,
            isAlreadyInDB = isInstituteInDB,
            autoCompleteTextView = binding.instituenameedittext,
            viewModel = addDetailResumeVM
        )
    }

    private fun handleClicks() {
        binding.checkItscontinue.setOnClickListener {
            binding.enddateTextInputLayout2.isEnabled = !binding.checkItscontinue.isChecked
            if (binding.checkItscontinue.isChecked) {
                binding.enddateedittext.setText("")
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
                saveEducation()
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun saveEducation() {
        endDate = if (binding.checkItscontinue.isChecked) {
            null
        } else {
            Helper.convertToUTCTimeForma(binding.enddateedittext.text.toString().trim())
        }
        val checkEndDate = userQualificationsList.filter { it.endDate == endDate }

        if (checkEndDate.isNotEmpty()) {
            AppsKitSDKUtils.makeToast("you had already added this end date")
        } else {
            val newQualification = ProfileModelAddDetailResponse.UserQualification(
                degree = degreePredictiveSearchHandler.getText(),
                institute = institutePredictiveSearchHandler.getText(),
                startDate = Helper.convertToUTCTimeForma(
                    binding.startdateedittext.text.toString().trim()
                ),
                endDate = endDate,
                qualificationType = "degree"
            )

            val updatedList =
                ArrayList<ProfileModelAddDetailResponse.UserQualification>(userQualificationsList)

            if (position != null) {
                updatedList[position] = newQualification
            } else {
                updatedList.add(newQualification)
            }
            callback.onEducationUpdated(updatedList)
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

}