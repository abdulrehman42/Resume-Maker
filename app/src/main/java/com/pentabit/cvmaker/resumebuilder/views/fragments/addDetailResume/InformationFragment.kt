package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
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
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class InformationFragment : AddDetailsBaseFragment<FragmentInformationBinding>() {
    var image = ""
    var getImage = ""
    var gender = ""
    var profile_id = ""
    private lateinit var selectedImageBitmap: Bitmap
    val looksAdapter = LooksAdapter()
    var data: ProfileModelAddDetailResponse? = null
    var defaulValueAddress: String? = null
    var defaulValuePhone: String? = null

    val addDetailResumeVM by viewModels<AddDetailResumeVM>()
    lateinit var tabhost: TabLayout

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override val inflate: Inflate<FragmentInformationBinding>
        get() = FragmentInformationBinding::inflate


    @RequiresApi(Build.VERSION_CODES.O)
    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(currentActivity()) {
            sharePref.writeDataProfile(it)
            setValue(it)
        }
        addDetailResumeVM.informationResponse.observe(viewLifecycleOwner)
        {
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(Constants.AUTH_TOKEN, it.token)
            sharePref.writeString(PROFILE_ID, it.profile.id.toString())
            tabhost.getTabAt(1)!!.select()
        }
        /*addDetailResumeVM.looksupResponse.observe(viewLifecycleOwner) {
            setAdapter(it)
        }*/
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun init(savedInstanceState: Bundle?) {
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        addDetailResumeVM.getProfileDetail(sharePref.readString(PROFILE_ID).toString())
        profile_id = currentActivity().intent.getStringExtra(Constants.CALL_API).toString()
        if (!profile_id.isNullOrEmpty()) {
            addDetailResumeVM.getProfileDetail(
                profile_id
            )

        }
        data = sharePref.readProfileData()
        data?.let {
            setValue(it)
        }
        onclick()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setValue(data: ProfileModelAddDetailResponse) {
        binding.apply {
            nameedittext.setText(data.name)
            jobedittext.setText(data.jobTitle)
            emailtext.setText(data.email)
            phoneedittext.setText(data.phone)
            address.setText(data.address)
            dobEdit.setText(Helper.convertIsoToCustomFormat(data.dob))
            if (data.gender == "male") {
                male()
            } else {
                female()
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
            male()
        }
        binding.editprofile.setOnClickListener {
            alertboxChooseImage(currentActivity()) {
                if (it == com.pentabit.cvmaker.resumebuilder.utils.Constants.CAMERA) {
                    openCamera()
                } else {
                    galleryOpen()
                }
            }
        }
        binding.woman.setOnClickListener {
            female()
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
            if (data != null) {
                if (isConditionMet2()) {
                    callApiUpdate(data!!.id.toString())
                }
            } else if (profile_id != null) {
                if (isConditionMet2()) {
                    callApiUpdate(profile_id)
                }
            } else if (isConditionMet()) {
                callApi()
            } else {
                currentActivity().showToast(getString(R.string.field_missing_error))
            }
        }
    }

    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.position, query, "", "")
    }

    private fun callApiUpdate(profileId: String) {
        addDetailResumeVM.updateProfile(
            profileId,
            CreateProfileRequestModel(
                binding.nameedittext.text.toString(),
                binding.emailtext.text.toString(),
                binding.phoneedittext.text.toString(),
                getImage,
                gender,
                binding.jobedittext.text.toString(),
                binding.dobEdit.text.toString(),
                binding.address.text.toString()
            )
        )
    }

    private fun callApi() {
        defaulValueAddress = if (binding.address.text.isNullOrEmpty()) {
            null
        } else {
            binding.address.text.toString()
        }
        defaulValuePhone = if (binding.phoneedittext.text.isNullOrEmpty()) {
            null
        } else {
            binding.phoneedittext.text.toString()
        }

        addDetailResumeVM.createProfile(
            CreateProfileRequestModel(
                binding.nameedittext.text.toString(),
                binding.emailtext.text.toString(),
                defaulValuePhone,
                getImage,
                gender,
                binding.jobedittext.text.toString(),
                binding.dobEdit.text.toString(),
                defaulValueAddress
            )
        )
    }

    fun isConditionMet(): Boolean {
        return !binding.nameedittext.text.toString().trim().isNullOrEmpty() &&
                Helper.isValidEmail(currentActivity(), binding.emailtext.text.toString()) &&
                !binding.jobedittext.text.toString().trim().isNullOrEmpty() &&
                !binding.dobEdit.text.toString().trim().isNullOrEmpty() &&
                !gender.isNullOrEmpty() &&
                !getImage.isNullOrEmpty()
    }

    fun isConditionMet2(): Boolean {
        return !binding.nameedittext.text.toString().trim().isNullOrEmpty() &&
                Helper.isValidEmail(currentActivity(), binding.emailtext.text.toString()) &&
                !binding.jobedittext.text.toString().trim().isNullOrEmpty() &&
                !binding.dobEdit.text.toString().trim().isNullOrEmpty() &&
                !gender.isNullOrEmpty()
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

    fun male() {
        binding.man.setBackgroundResource(R.drawable.bluebgradius)
        binding.woman.setBackgroundResource(R.drawable.greybgradius)
        gender = getString(R.string.male)
        binding.woman.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.light_black
            )
        )
        binding.man.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

    }

    fun female() {
        binding.man.setBackgroundResource(R.drawable.greybgradius)
        binding.woman.setBackgroundResource(R.drawable.bluebgradius)
        gender = getString(R.string.female)
        binding.woman.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        binding.man.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))

    }

}