package com.pentabit.cvmaker.resumebuilder.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityBoardingScreenBinding
import com.pentabit.cvmaker.resumebuilder.models.TemplateModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.views.adapter.WebViewPrintAdapter
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.callbacks.OnImageCompressed
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
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

    fun getFilePathFromUri(context: Context, uri: Uri): String? {
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

    fun removeOneUnderscores(input: String): String {
        return input.replace("1__", "")
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun convertIsoToCustomFormat(isoDateTime: String?): String {
        // Parse the ISO_DATE_TIME string to a LocalDateTime object
        val dateTime = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)

        // Define the desired format
        val customFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")

        // Format the LocalDateTime to the desired format
        return dateTime.format(customFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateRangeYearOnly(startDate: String?, endDate: String?): String {
        if (startDate.isNullOrEmpty()) {
            return "Invalid year range"
        }

        val inputFormatter = DateTimeFormatter.ISO_DATE_TIME
        val yearFormatter = DateTimeFormatter.ofPattern("yyyy")

        return try {
            var date = ""
            var startYear = ""
            val start = ZonedDateTime.parse(startDate, inputFormatter).toLocalDate()
            if (endDate.isNullOrEmpty()) {
                date = start.format(yearFormatter)
            } else {
                val end = ZonedDateTime.parse(endDate, inputFormatter).toLocalDate()
                startYear = start.format(yearFormatter)

                val endYear = end.format(yearFormatter)

                date = "$startYear - $endYear"
            }
            return date

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
        val name =
            RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.name)
        val email =
            RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.email)
        val phone = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            createProfileRequestModel.phone.toString()
        )
        val gender =
            RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.gender)
        val job =
            RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.jobTitle)
        val dob =
            RequestBody.create("text/plain".toMediaTypeOrNull(), createProfileRequestModel.dob)
        val address = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            createProfileRequestModel.address.toString()
        )

        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
            "gender" to gender,
            "jobTitle" to job,
            "dob" to dob,
            "address" to address
        )
    }

    fun getImagesFromResumeMakerFolder(context: Context): List<File> {
        val images = mutableListOf<File>()
        val directory = context.getDir(context.getString(R.string.app_name), Context.MODE_PRIVATE)

        // Check if the directory exists and is a directory
        if (directory.exists() && directory.isDirectory) {
            // Get all files in the directory
            val files = directory.listFiles()

            // Filter image files based on common image file extensions
            if (files != null) {
                for (file in files) {
                    if (file.isFile && file.extension in listOf(
                            "jpg",
                            "jpeg",
                            "png",
                            "gif",
                            "bmp"
                        )
                    ) {
                        images.add(file)
                    }
                }
            }
        }
        return images
    }


    fun saveHtmlToInternalStorage(
        webView: WebView,
        htmlContent: String,
        context: Context,
        fileName: String
    ) {
        // Configure WebView settings if needed
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        // Load the HTML content
        webView.loadDataWithBaseURL(null, htmlContent, "text/html", "UTF-8", null)

        // Set WebViewClient to handle page load events
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                view?.post {
                    // Wait for WebView to finish rendering
                    view?.measure(
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                    )
                    view?.layout(0, 0, view.measuredWidth, view.measuredHeight)

                    // Create a bitmap with the dimensions of the WebView
                    val bitmap = Bitmap.createBitmap(
                        view.measuredWidth,
                        view.measuredHeight,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(bitmap)
                    view.draw(canvas)

                    // Save the bitmap as a JPG file
                    try {
                        val directory = context.getDir("Resume Maker", Context.MODE_PRIVATE)
                        if (!directory.exists()) {
                            directory.mkdir() // Ensure directory exists
                        }
                        val file = File(directory, "$fileName.jpg")

                        FileOutputStream(file).use { out ->
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        }

                        println("Image saved successfully at ${file.absolutePath}")
                    } catch (e: IOException) {
                        e.printStackTrace()
                        println("Error saving image: ${e.message}")
                    }
                }
            }
        }
    }


    fun saveHtmlAsPdf(context: Context, htmlContent: String, fileName: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager
        val printAdapter = WebViewPrintAdapter(context, htmlContent)

        printManager.print("Document", printAdapter, PrintAttributes.Builder().build())
    }
}
