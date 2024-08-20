package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInformationBinding
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Constants.IMAGE_CODE
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.DialogueBoxes.alertboxChooseImage
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

@AndroidEntryPoint
class InformationFragment : AddDetailsBaseFragment<FragmentInformationBinding>() {
    private lateinit var selectedImageBitmap: Bitmap
    var image=""
    var gender="male"
    val addDetailResumeVM by viewModels<AddDetailResumeVM>()
    lateinit var tabhost:TabLayout

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override val inflate: Inflate<FragmentInformationBinding>
        get() = FragmentInformationBinding::inflate


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner)
        {
            sharePref.writeInteger(Constants.PROFILE_ID,it.id)
            tabhost.getTabAt(1)!!.select()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        tabhost= currentActivity().findViewById(R.id.tab_layout_adddetail)!!

        onclick()
    }

    @SuppressLint("ResourceAsColor")
    private fun onclick() {
        binding.dobEdit.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    // The EditText gained focus
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                // Handle the result here
                                binding.dobEdit.setText(date)
                            }
                        })
                }
            }
        binding.man.setOnClickListener {
            binding.man.setBackgroundResource(R.drawable.bluebgradius)
            binding.woman.setBackgroundResource(R.drawable.greybgradius)
            gender=getString(R.string.male)
            binding.woman.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.light_black
                )
            )
            binding.man.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))

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
            binding.man.setBackgroundResource(R.drawable.greybgradius)
            binding.woman.setBackgroundResource(R.drawable.bluebgradius)
            gender=getString(R.string.female)
            binding.woman.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            binding.man.setTextColor(ContextCompat.getColor(requireContext(), R.color.light_black))
        }
        binding.nextbtn.setOnClickListener {

            if (isConditionMet()) {
               // callApi()
                tabhost.getTabAt(1)!!.select()

            } else {
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
    }

    private fun callApi() {
        addDetailResumeVM.createProfile(
            CreateProfileRequestModel(
            binding.nameedittext.text.toString(),binding.emailtext.text.toString(),
            binding.phoneedittext.text.toString(),image,gender,binding.jobedittext.text.toString(),
            binding.dobEdit.text.toString(),binding.address.text.toString())
        )
    }

    fun isConditionMet(): Boolean {
        return !binding.nameedittext.text.toString().trim().isNullOrEmpty() &&
                !binding.emailtext.text.toString().trim().isNullOrEmpty() &&
                !binding.jobedittext.text.toString().trim().isNullOrEmpty() &&
                !binding.phoneedittext.text.toString().trim().isNullOrEmpty()
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
                image= selectedImageBitmap.toString()
                /*val stream = ByteArrayOutputStream()
                selectedImageBitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream)
                val bytes: ByteArray = stream.toByteArray()
                image = Base64.encodeToString(bytes, Base64.DEFAULT)*/

            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_CODE) {
            image = (data!!.extras!!["data"] as Bitmap?).toString()
            Glide.with(requireActivity()).load(image).into(binding.shapeableImageView)
            /*  val stream = ByteArrayOutputStream()
              photo?.compress(Bitmap.CompressFormat.JPEG, 60, stream)
              val bytes: ByteArray = stream.toByteArray()
              image = Base64.encodeToString(bytes, Base64.DEFAULT)
   */       }
    }


}