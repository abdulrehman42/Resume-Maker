package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseImage
import com.pentabit.cvmaker.resumebuilder.utils.Helper.getFileFromUri
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInformationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
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
    private var looksAdapter = LooksAdapter()
    private var isEditProfile = false
    private var isProgrammaticallySettingText = false
    val adapter = ""
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
        addDetailResumeVM.looksupResponse.observe(this) {
            looksAdapter.submitList(it)
            if (it.size == 0)
                binding.lookidRecyclerview.isGone = true
            else
                binding.lookidRecyclerview.isGone = false
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }


    override fun init(savedInstanceState: Bundle?) {
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        isEditProfile = requireActivity().intent.getBooleanExtra(Constants.IS_EDIT, false)
        if (isEditProfile) {
            addDetailResumeVM.getProfileDetail()
        }

        binding.lookidRecyclerview.apply {
            adapter = looksAdapter
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

    private fun onclick() {
        binding.dobEdit.setOnClickListener {
            DialogueBoxes.showWheelDatePickerDialogDOB(
                currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.dobEdit.setText(date)
                    }
                }
            )
        }
        looksAdapter.setOnItemClickCallback {
            binding.jobedittext.setText(Helper.removeOneUnderscores("1__" + it.text))
           // callLookUpApi(null)
            isProgrammaticallySettingText=true
            binding.lookidRecyclerview.isGone = true
        }


        binding.man.setOnClickListener {
            manageGenderSelection(getString(R.string.male))
        }

        binding.editprofile.setOnClickListener {
            alertboxChooseImage(currentActivity()) {
                if (it == Constants.CAMERA) {
                    if (ContextCompat.checkSelfPermission(
                            requireContext(), Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED && (requireActivity() as AddDetailResume).checkReadPermission()
                    ) openCamera()
                    else {
                        (requireActivity() as AddDetailResume).askCameraPermission()
                        (requireActivity() as AddDetailResume).askReadWritePermission()
                    }
                } else {
                    if ((requireActivity() as AddDetailResume).checkReadPermission())
                        galleryOpen()
                    else {
                        (requireActivity() as AddDetailResume).askReadWritePermission()
                    }
                }
            }
        }
        binding.woman.setOnClickListener {
            manageGenderSelection(getString(R.string.female))
        }

        binding.jobedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

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
                    if (s.isNullOrEmpty()) {
                        callLookUpApi(null) // Send null query when text is erased
                        binding.lookidRecyclerview.isGone = true
                    }
                }
                isProgrammaticallySettingText=false

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

    private fun callLookUpApi(query: String?) {
        addDetailResumeVM.getLookUp(Constants.position, query, "", "10")
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
        var address: String? = null
        address = binding.address.text.toString()
        if (address.isEmpty()) {
            address = null
        }
        return CreateProfileRequestModel(
            binding.nameedittext.text.toString(),
            binding.emailtext.text.toString(),
            binding.phoneedittext.text.toString(),
            getImage,
            gender,
            binding.jobedittext.text.toString(),
            binding.dobEdit.text.toString(),
            address
        )
    }

    private fun isAllRequiredFieldsComplete(): Boolean {
        return isFieldNotNullOrEmpty(binding.nameedittext, binding.jobedittext, binding.dobEdit) &&
                Helper.isValidEmail(binding.emailtext.text.toString())&&
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


    private val galleryResultLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                try {
                    selectedImageBitmap =
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, it)
                    binding.shapeableImageView.setImageBitmap(selectedImageBitmap)
                    image = selectedImageBitmap.toString()
                    getImage = getFileFromUri(currentActivity(), it).toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }


    private var cameraPhotoUri: Uri? = null

    @RequiresPermission(Manifest.permission.CAMERA)
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraPhotoUri = requireActivity().contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues()
        )
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri)
        takePhotoLauncher.launch(cameraPhotoUri!!)
    }

    private val takePhotoLauncher: ActivityResultLauncher<Uri> = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { result ->
        if (java.lang.Boolean.TRUE == result) {
            if (cameraPhotoUri != null) {
                Glide.with(requireActivity()).load(cameraPhotoUri).into(binding.shapeableImageView)
                getImage = getFileFromUri(currentActivity(), cameraPhotoUri!!).toString()
            }
        }
    }

    private fun galleryOpen() {
        galleryResultLauncher.launch("image/*")
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