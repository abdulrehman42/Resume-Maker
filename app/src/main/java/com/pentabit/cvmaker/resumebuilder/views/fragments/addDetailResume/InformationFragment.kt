package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInformationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Genders
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.Helper.getFileFromUri
import com.pentabit.cvmaker.resumebuilder.utils.ImageSourceSelectionHelper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationFragment() :

    AddDetailsBaseFragment<FragmentInformationBinding>() {
    private var getImage = ""
    private var gender: Genders? = null
    private lateinit var jobTitlePredictiveSearchHandler: PredictiveSearchHandler
    private var isJobTitleFromDB = false
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()

    override val inflate: Inflate<FragmentInformationBinding>
        get() = FragmentInformationBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        Validations.limitEditTextCharacters(binding.phoneedittext, 16)
        if (addDetailResumeVM.isInEditMode) {
            isJobTitleFromDB = true
            fetchProfileInfo()
        } else {
            manageLookUpsAdapter()
        }
        handleClicks()
    }


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            populateProfileInfo(it)
            manageLookUpsAdapter()
            (currentActivity() as AddDetailResume).requiredItemsAddInTab(it)
        }

    }

    private fun manageLookUpsAdapter() {
        jobTitlePredictiveSearchHandler =
            PredictiveSearchHandler(
                key = Constants.position,
                isAlreadyInDB = isJobTitleFromDB,
                autoCompleteTextView = binding.jobedittext,
                viewModel = addDetailResumeVM
            )
    }

    private fun handleClicks() {
        binding.editprofile.setOnClickListener {
            (currentActivity() as AddDetailResume).imageSourceSelectionHelper.onCreateWallpaperClicked(
                object :
                    ImageSourceSelectionHelper.OnImageSelected {
                    override fun onImageSelected(uri: Uri) {
                        Glide.with(ResumeMakerApplication.instance).load(uri)
                            .into(binding.shapeableImageView)
                        getImage = getFileFromUri(currentActivity(), uri).toString()
                    }
                })
        }


        binding.dobEdit.setOnClickListener {
            DialogueBoxes.showWheelDatePickerDialogDOB(currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.dobEdit.setText(date)
                    }
                })
        }

        binding.man.setOnClickListener {
            manageGenderSelection(Genders.MALE)
        }

        binding.woman.setOnClickListener {
            manageGenderSelection(Genders.FEMALE)
        }

    }

    private fun fetchProfileInfo() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun createOrUpdateProfileInfo(createProfileInfoModel: CreateProfileRequestModel) {
        if (addDetailResumeVM.isInEditMode) {
            addDetailResumeVM.updateProfile(
                createProfileInfoModel
            )
        } else {
            addDetailResumeVM.createProfile(
                createProfileInfoModel
            )
        }
    }

    private fun getProfileModel(): CreateProfileRequestModel {
        var address: String?
        var phone: String?
        address = binding.address.text.toString()
        phone = binding.phoneedittext.text.toString()
        if (address.isEmpty()) {
            address = null
        }
        if (phone.isEmpty()) {
            phone = null
        }
        return CreateProfileRequestModel(
            binding.nameedittext.text.toString(),
            binding.emailtext.text.toString(),
            phone,
            getImage,
            gender!!.gender,
            jobTitlePredictiveSearchHandler.getText(),
            binding.dobEdit.text.toString(),
            address
        )
    }

    private fun populateProfileInfo(data: ProfileModelAddDetailResponse) {
        binding.apply {
            nameedittext.setText(data.name)
            jobedittext.setText(Helper.removeOneUnderscores(data.jobTitle))
            emailtext.setText(data.email)
            phoneedittext.setText(data.phone)
            if (data.address == "null") {
                address.setText("--")
            } else if (!data.address.isNullOrEmpty()) {
                address.setText(data.address)
            } else {
                address.setText("--")
            }
            dobEdit.setText(Helper.convertIsoToCustomFormat(data.dob))
            manageGenderSelection(Genders.findGender(data.gender))
            Glide.with(ResumeMakerApplication.instance).load(Constants.BASE_MEDIA_URL + data.path)
                .placeholder(R.drawable.imgplaceholder).into(shapeableImageView)
        }
    }

    private fun manageGenderSelection(selectedGender: Genders) {
        gender = selectedGender
        if (gender == Genders.MALE) {
            handleGenderSelectionUI(binding.man, binding.woman)
        } else {
            handleGenderSelectionUI(binding.woman, binding.man)
        }
    }

    private fun handleGenderSelectionUI(selected: TextView, unselected: TextView) {
        selected.setBackgroundResource(R.drawable.bluebgradius)
        selected.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        unselected.setBackgroundResource(R.drawable.greybgradius)
        unselected.setTextColor(
            ContextCompat.getColor(
                requireContext(), R.color.light_black
            )
        )
    }


    override fun csnMoveForward(): Boolean {
        return Validations.infoScreenValidations(
            binding,
            gender,
            addDetailResumeVM.isInEditMode,
            getImage
        )
    }


    override fun onMoveNextClicked(): Boolean {
        if (csnMoveForward()) {
            createOrUpdateProfileInfo(getProfileModel())
        }
        return false
    }

}