package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IMAGE_CODE
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseImage
import com.pentabit.cvmaker.resumebuilder.utils.Helper.getFileFromUri
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInformationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class InformationFragment : AddDetailsBaseFragment<FragmentInformationBinding>() {
    private var image = ""
    private var getImage = ""
    private var gender = ""
    private lateinit var selectedImageBitmap: Bitmap
    private val looksAdapter = LooksAdapter()
    private var isEditProfile = false
    val addDetailResumeVM by viewModels<AddDetailResumeVM>()
    lateinit var tabhost: TabLayout

    override fun csnMoveForward(): Boolean {
        return isAllRequiredFieldsComplete()
    }

    override val inflate: Inflate<FragmentInformationBinding>
        get() = FragmentInformationBinding::inflate


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(currentActivity()) {
            setValue(it)
        }

        addDetailResumeVM.informationResponse.observe(viewLifecycleOwner)
        {
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(PROFILE_ID, it.id.toString())
            tabhost.getTabAt(1)!!.select()
        }

        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    private fun setAdapter(lookUpResponses: List<LookUpResponse>) {
        looksAdapter.submitList(lookUpResponses)
        binding.lookidRecyclerview.apply {
            isGone = false
            adapter = looksAdapter
        }
        looksAdapter.setOnItemClickCallback {
            binding.jobedittext.setText(it.text)
            binding.lookidRecyclerview.isGone = true
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        isEditProfile = requireActivity().intent.getBooleanExtra(Constants.IS_EDIT, false)
        if (isEditProfile) {
            addDetailResumeVM.getProfileDetail()
        }

        onclick()
    }


    private fun setValue(data: ProfileModelAddDetailResponse) {
        binding.apply {
            nameedittext.setText(data.name)
            jobedittext.setText(data.jobTitle)
            emailtext.setText(data.email)
            phoneedittext.setText(data.phone)
            address.setText(data.address)
            dobEdit.setText(Helper.convertIsoToCustomFormat(data.dob))
            if (data.gender == getString(R.string.male)) {
                manageGenderSelection(getString(R.string.male))
            } else {
                manageGenderSelection(getString(R.string.female))
            }
        }
        Glide.with(currentActivity()).load(Constants.BASE_MEDIA_URL + data.path)
            .placeholder(R.drawable.imgplaceholder).into(binding.shapeableImageView)
    }

    @SuppressLint("ResourceAsColor")
    private fun onclick() {
        binding.dobEdit.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                binding.dobEdit.setText(date)
                            }
                        })
                }
            }

        binding.man.setOnClickListener {
            manageGenderSelection(getString(R.string.male))
        }

        binding.editprofile.setOnClickListener {
            alertboxChooseImage(currentActivity()) {
                if (it == Constants.CAMERA) {
                    openCamera()
                } else {
                    galleryOpen()
                }
            }
        }
        binding.woman.setOnClickListener {
            manageGenderSelection(getString(R.string.female))
        }

        binding.jobedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = binding.jobedittext.text.toString()
                //   callLookUpApi(query)
            }

            override fun afterTextChanged(s: Editable?) {
                val query = binding.jobedittext.text.toString()
                //    callLookUpApi(query)
            }
        })

        binding.nextbtn.setOnClickListener {
            if (isAllRequiredFieldsComplete()) {
                if (isEditProfile)
                    callApiUpdate()
                else
                    callApi()
            } else {
                currentActivity().showToast(getString(R.string.field_missing_error))
            }
        }
    }

    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.position, query, "", "")
    }

    private fun callApiUpdate() {
        addDetailResumeVM.updateProfile(
            grtProfileModel()
        )
    }

    private fun callApi() {
        addDetailResumeVM.createProfile(
            grtProfileModel()
        )
    }

    fun grtProfileModel(): CreateProfileRequestModel {
        return CreateProfileRequestModel(
            binding.nameedittext.text.toString(),
            binding.emailtext.text.toString(),
            binding.phoneedittext.text.toString(),
            getImage,
            gender,
            binding.jobedittext.text.toString(),
            binding.dobEdit.text.toString(),
            binding.address.text.toString()
        )
    }

    private fun isAllRequiredFieldsComplete(): Boolean {
        return isFieldNotNullOrEmpty(binding.nameedittext, binding.jobedittext, binding.dobEdit) &&
                Helper.isValidEmail(binding.emailtext.text.toString()) &&
                !gender.isNullOrEmpty() &&
                (isEditProfile || !getImage.isNullOrEmpty())
    }


    private fun isFieldNotNullOrEmpty(vararg views: TextView): Boolean {
        for (v in views) {
            if (v.text.toString().trim().isEmpty()) {
                return false
            }
        }
        return true
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, IMAGE_CODE)
    }


    fun galleryOpen() {
        val i = Intent()
        i.type = "image/*"
        i.action = Intent.ACTION_GET_CONTENT
        launchSomeActivity.launch(i)
    }

    var launchSomeActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode
            == RESULT_OK
        ) {
            val data = result.data
            if (data != null
                && data.data != null
            ) {
                val selectedImageUri = data.data
                try {
                    selectedImageBitmap = MediaStore.Images.Media.getBitmap(
                        requireActivity().contentResolver,
                        selectedImageUri
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.shapeableImageView.setImageBitmap(selectedImageBitmap)
                image = selectedImageBitmap.toString()
                val uri: Uri? = data?.data
                getImage = uri?.let { getFileFromUri(currentActivity(), it) }.toString()
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE) {
            val uri: Uri? = data?.data
            getImage = uri?.let { getFileFromUri(currentActivity(), it) }.toString()
            Glide.with(requireActivity()).load(uri).into(binding.shapeableImageView)

        }
    }

    private fun manageGenderSelection(gend: String) {
        gender = gend
        if (gender == getString(R.string.male)) {
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
                requireContext(),
                R.color.light_black
            )
        )
    }


}