package com.pentabit.cvmaker.resumebuilder.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.print.PrintAttributes
import android.print.PrintManager
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isGone
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityBoardingScreenBinding
import com.pentabit.cvmaker.resumebuilder.models.TemplateModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.views.activities.LoginActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.WebViewPrintAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone


object Helper {
    fun updateCircleMarker(binding: ActivityBoardingScreenBinding, position: Int) {
        when (position) {
            0 -> {
                binding.dot1.setImageResource(R.drawable.blue_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.skipbtn.setText(R.string.skip)
                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            1 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.blue_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.skipbtn.setText(R.string.skip)

                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            2 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.blue_dot)
                binding.dot4.setImageResource(R.drawable.grey_dot)
                binding.skipbtn.setText(R.string.skip)

                binding.nextbtn.setText("Next")
                binding.skipbtn.isGone = false
            }

            3 -> {
                binding.dot1.setImageResource(R.drawable.grey_dot)
                binding.dot2.setImageResource(R.drawable.grey_dot)
                binding.dot3.setImageResource(R.drawable.grey_dot)
                binding.dot4.setImageResource(R.drawable.blue_dot)
                binding.skipbtn.setText("")
                binding.nextbtn.setText("Done")

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
        if (startDate.isNullOrEmpty()) {
            return "Invalid date range"
        }

        // Define input and output formatters
        val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME // Adjusted to handle offsets
        val outputFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

        return try {
            // Parse start date with the input formatter
            val start = ZonedDateTime.parse(startDate, inputFormatter).toLocalDate()
            val startFormatted = start.format(outputFormatter)

            // If endDate is null, return "startFormatted - Present"
            if (endDate.isNullOrEmpty()) {
                return "$startFormatted - Present"
            }

            // Parse end date with the input formatter
            val end = ZonedDateTime.parse(endDate, inputFormatter).toLocalDate()
            val endFormatted = end.format(outputFormatter)

            "$startFormatted - $endFormatted"
        } catch (e: DateTimeParseException) {
            // Handle specific parsing errors
            "Invalid date format: ${e.message}"
        } catch (e: Exception) {
            // Handle any other unexpected exceptions
            "An error occurred: ${e.message}"
        }
    }

    fun isValidEmail(email: String): Boolean {

        if (email.isEmpty()) {
            AppsKitSDKUtils.makeToast("Please Enter Your Email")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            AppsKitSDKUtils.makeToast("Please Enter Your Correct Email")
            if (TextUtils.isEmpty(email)) {
                AppsKitSDKUtils.makeToast("Please Enter Email")
                return false
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                AppsKitSDKUtils.makeToast("Please Correct Your Email")
                return false
            }
        }
        return true
    }

    fun showToast(context: Context, message: String?) {
        message?.let {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            Toast.makeText(
                context,
                context.getString(R.string.something_went_wrong),
                Toast.LENGTH_SHORT
            )
                .show()
        }

    }


    fun removeOneUnderscores(input: String): String {
        return input.replace(Regex("[\\d_+\\-*/]"), "")

    }


    fun convertIsoToCustomFormat(isoDateTime: String?): String {
        // Check if the input string is null or blank
        if (isoDateTime.isNullOrBlank()) return ""


        // Define the ISO 8601 date format
        val isoFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        isoFormatter.timeZone = TimeZone.getTimeZone("UTC")

        // Parse the ISO 8601 date string into a Date object
        val date = isoFormatter.parse(isoDateTime) ?: return ""

        // Get the current time
        val now = Calendar.getInstance().time

        // Check if the parsed date is in the future
        if (date.after(now)) {
            return "" // or throw IllegalArgumentException("Future dates are not allowed.")
        }

        // Define the desired output format
        val customFormatter = SimpleDateFormat("M/d/yyyy", Locale.getDefault())

        // Format the Date object to the desired format
        return customFormatter.format(date)


        // Parse the ISO_DATE_TIME string to a LocalDateTime object
//        val dateTime = LocalDateTime.parse(isoDateTime, DateTimeFormatter.ISO_DATE_TIME)

        // Check if the parsed dateTime is in the future
//        if (dateTime.isAfter(LocalDateTime.now())) {
//            // Handle future date according to your needs
//            // For example, return an empty string or throw an exception
//            return "" // or throw IllegalArgumentException("Future dates are not allowed.")
//        }
//
//        // Define the desired format
//        val customFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
//
//        // Format the LocalDateTime to the desired format
//        return dateTime.format(customFormatter)
    }

    fun formatDateRangeYearOnly(startDate: String?, endDate: String?): String {
        if (startDate.isNullOrEmpty()) {
            return "Invalid year range"
        }

        // Use SimpleDateFormat for older Android versions
        val inputFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.getDefault())
        val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

        return try {
            val start = inputFormatter.parse(startDate)
            val startYear = yearFormatter.format(start)

            if (endDate.isNullOrEmpty()) {
                "$startYear - Present"
            } else {
                val end = inputFormatter.parse(endDate)
                val endYear = yearFormatter.format(end)
                "$startYear - $endYear"
            }
        } catch (e: Exception) {
            "Invalid date format"
        }
    }

    fun getFileFromUri(context: Context, uri: Uri): String? {
        return when (uri.scheme) {
            "content" -> copyUriContentToFile(context, uri)
            "file" -> uri.path
            else -> null
        }
    }

    private fun copyUriContentToFile(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            inputStream?.let {
                val tempFile = File(context.cacheDir, "temp_image_${System.currentTimeMillis()}")
                FileOutputStream(tempFile).use { outputStream ->
                    val buffer = ByteArray(1024)
                    var length: Int
                    while (inputStream.read(buffer).also { length = it } > 0) {
                        outputStream.write(buffer, 0, length)
                    }
                    outputStream.flush()
                }
                tempFile.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
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

    fun share_Image(activity: Activity,getimage:String) {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        val bundle: Bundle? = activity.intent.extras
        val image = bundle!!.getString(getimage)
        val im=activity.findViewById<ImageView>(R.id.share)
        val bitmapdrawer: BitmapDrawable = im.drawable as BitmapDrawable
        val bitmap: Bitmap = bitmapdrawer.bitmap
        val dir=File(image)
        try {
            val filout = FileOutputStream(dir)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, filout)
            filout.flush()
            filout.close()
            var shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "Image/*"
            shareIntent.putExtra(Intent.EXTRA_STREAM, dir)
            shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            activity.startActivity(Intent.createChooser(shareIntent,"share image"))
        } catch (e: Exception) {
            AppsKitSDKUtils.makeToast("$e")
        }
    }
}