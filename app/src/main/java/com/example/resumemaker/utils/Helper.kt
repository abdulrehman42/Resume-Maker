package com.example.resumemaker.utils

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import com.example.resumemaker.R
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.models.BoardingItems
import com.example.resumemaker.models.TemplateModel
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.google.android.material.tabs.TabLayout
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

object Helper {
    fun updateCircleMarker(binding: ActivityBoardingScreenBinding, position: Int) {
        when (position) {
            0 -> {
                binding.dot1.setImageResource(R.drawable.blue_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            1 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.blue_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            2 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.blue_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            3 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.blue_dot)
                binding.nextbtn.setText("Done")
                binding.skipbtn.isGone = true

            }
        }
    }

    fun getTabView(context: Context, position: Int): View {
        val view = LayoutInflater.from(context).inflate(R.layout.customtablayout, null)
        val tabIcon = view.findViewById<ImageView>(R.id.nav_icon)
        val tabTitle = view.findViewById<TextView>(R.id.nav_label)

        when (position) {
            0 -> {
                tabIcon.setImageResource(R.drawable.info)
                tabTitle.text = "Info"
            }

            1 -> {
                tabIcon.setImageResource(R.drawable.objectives)
                tabTitle.text = "Objectives"
            }

            2 -> {
                tabIcon.setImageResource(R.drawable.education)
                tabTitle.text = "Education"
            }

            3 -> {
                tabIcon.setImageResource(R.drawable.skill)
                tabTitle.text = "Skill"
            }

            4 -> {
                tabIcon.setImageResource(R.drawable.experience)
                tabTitle.text = "Experience"
            }

            5 -> {
                tabIcon.setImageResource(R.drawable.referrence)
                tabTitle.text = "reference"
            }
        }
        return view
    }

    private fun setTabSelected(tab: TabLayout.Tab, isSelected: Boolean) {
        val tabView = tab.customView
        val textView = tabView?.findViewById<TextView>(R.id.nav_label)
        textView?.setTextColor(if (isSelected) Color.WHITE else Color.GRAY)
        // Optionally, change other attributes like background, text style, etc.
    }


    fun getTemplateImages(): List<TemplateModel> {
        val list = ArrayList<TemplateModel>()
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        list.add(TemplateModel(R.drawable.resume_placeholder))
        return list
    }

    fun Int.dpToPx(context: Context): Int {
        return (this * context.resources.displayMetrics.density).toInt()
    }

    /* val endIconView = findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)
               val params = endIconView.layoutParams as FrameLayout.LayoutParams
               params.gravity = Gravity.BOTTOM or Gravity.END
               params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
               params.bottomMargin = 8.dpToPx(context)
               endIconView.layoutParams = params*/

    fun getFilePathFromUri(context: Context,uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (it.moveToFirst()) {
                return it.getString(columnIndex)
            }
        }
        return null
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateRange(startDate: String?, endDate: String?): String {
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            return "Invalid date range"
        }

        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val outputFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

        return try {
            val start = ZonedDateTime.parse(startDate, inputFormatter).toLocalDate()
            val end = ZonedDateTime.parse(endDate, inputFormatter).toLocalDate()

            val startFormatted = start.format(outputFormatter)
            val endFormatted = end.format(outputFormatter)

            "$startFormatted - $endFormatted"
        } catch (e: Exception) {
            "Invalid date format"
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateRangeYearOnly(startDate: String?, endDate: String?): String {
        if (startDate.isNullOrEmpty() || endDate.isNullOrEmpty()) {
            return "Invalid year range"
        }

        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

        return try {
            val start = ZonedDateTime.parse(startDate, inputFormatter).toLocalDate()
            val end = ZonedDateTime.parse(endDate, inputFormatter).toLocalDate()

            val startYear = start.format(yearFormatter)
            val endYear = end.format(yearFormatter)

            "$startYear - $endYear"
        } catch (e: Exception) {
            "Invalid date format"
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            if (cursor.moveToFirst()) {
                return cursor.getString(columnIndex)
            }
        }
        return null
    }


    fun prepareMultipartRequest(createProfileRequestModel: CreateProfileRequestModel): Map<String, Any> {
        val name = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.name)
        val email = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.email)
        val phone = RequestBody.create("text/plain".toMediaTypeOrNull(),
            createProfileRequestModel.phone.toString()
        )
        val gender = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.gender)
        val job = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.jobTitle)
        val dob = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.dob)
        val address = RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.address.toString())

        val imageFile = File(createProfileRequestModel.image)
        val imageRequestBody = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageFile)
        val imagePart = MultipartBody.Part.createFormData("image", imageFile.name, imageRequestBody)

        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "gender" to gender,
            "job" to job,
            "dob" to dob,
            "address" to address,
            "image" to imagePart
        )
    }
}
