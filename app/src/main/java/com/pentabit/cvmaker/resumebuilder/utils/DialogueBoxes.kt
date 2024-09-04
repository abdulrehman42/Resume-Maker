package com.pentabit.cvmaker.resumebuilder.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebViewClient
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.databinding.AddRemoveBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChooseEditProfileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChooseImageLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChoosecreateprofileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChoosedownloadLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.CreationdialogueBinding
import com.pentabit.cvmaker.resumebuilder.databinding.DeleteAccountBinding
import com.pentabit.cvmaker.resumebuilder.databinding.DeleteitemalertdialogueBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ImportProfileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.LogoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.PreviewdownloadBinding
import com.pentabit.cvmaker.resumebuilder.databinding.RatingLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.TemplateSelectLayoutBinding
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import java.util.Calendar


object DialogueBoxes {
    interface DialogCallback {
        fun onButtonClick(isConfirmed: Boolean)
    }

    interface StringDialogCallback {
        fun onButtonClick(date: String)
    }

    interface StringValueDialogCallback {
        fun onButtonClick(value: String)
    }

    fun alertbox(
        template: Any,
        currentActivity: Activity,
        param: com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.DialogCallback
    ) {
        val binding = TemplateSelectLayoutBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        Glide.with(currentActivity)
            .load(com.pentabit.cvmaker.resumebuilder.utils.Constants.BASE_MEDIA_URL + template)
            .into(binding.templateimage)
        binding.linearbtn.setOnClickListener {
            param.onButtonClick(true)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }


    fun deleteItemPopup(curentactivity: Activity, msg: String, param: DialogCallback) {
        val binding = DeleteitemalertdialogueBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.textshow.setText(msg)
        binding.yesBtn.setOnClickListener {
            param.onButtonClick(true)
            dialogBuilder.dismiss()
        }
        binding.noBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxPdf(template: String, currentActivity: Activity) {

        val binding = PreviewdownloadBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.templateimage.webViewClient = WebViewClient()
        binding.templateimage.loadDataWithBaseURL(
            Constants.BASE_URL_DEVELOPMENT,
            template,
            "text/html",
            "UTF-8",
            null
        )
        binding.linearbtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()

    }

    fun alertboxRate(curentactivity: Activity, screen: ScreenIDs) {
        AppsKitSDKUtils.showRateUsCustomDialog(
            curentactivity,
            screen.id,
            R.drawable.rate_us_dialog_background,
            R.drawable.ratting_selected,
            R.drawable.rating_unselected,
            R.drawable.rating_headericon,
            R.drawable.rate_us_bg,
            ContextCompat.getColor(curentactivity, R.color.white),
            ContextCompat.getColor(curentactivity, R.color.trans_white),
            ContextCompat.getColor(curentactivity, R.color.white),
            ContextCompat.getColor(curentactivity, R.color.white),
            curentactivity.getString(R.string.are_you_enjoying_our_app),
            curentactivity.getString(R.string.sharing_your_experience_helps_us_grow),
            curentactivity.getString(R.string.rate_us_txt),
            curentactivity.getString(R.string.may_be_later)
        )
//        val binding = RatingLayoutBinding.inflate(curentactivity.layoutInflater)
//        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
//        dialogBuilder.setContentView(binding.root)
//        binding.rateBtn.setOnClickListener {
//
//            dialogBuilder.dismiss()
//        }
//        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
//        dialogBuilder.setCancelable(true)
//        dialogBuilder.show()
    }

    fun alertboxPurchase(curentactivity: Activity, param: DialogCallback) {
        val binding = AddRemoveBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.removeAddBtn.setOnClickListener {
            param.onButtonClick(true)
            dialogBuilder.dismiss()
        }
        binding.maybeLaterBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun backPressedcheck(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = LogoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.textView7.text = ""
        binding.textView15.text = curentactivity.getString(R.string.back_questiom)
        binding.yesBtn.setOnClickListener {
            param.onButtonClick(Constants.YES)
            dialogBuilder.dismiss()
        }
        binding.noBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxLogout(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = LogoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.yesBtn.setOnClickListener {
            param.onButtonClick(Constants.YES)
            dialogBuilder.dismiss()
        }
        binding.noBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxDelete(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = DeleteAccountBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.yesBtn.setOnClickListener {
            param.onButtonClick(Constants.YES)
            dialogBuilder.dismiss()
        }
        binding.noBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxChooseImage(curentactivity: Activity, onclick: (String) -> Unit) {
        val binding = ChooseImageLayoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.camerbtn.setOnClickListener {
            onclick(Constants.CAMERA)
            dialogBuilder.dismiss()
        }
        binding.gallerybtn.setOnClickListener {
            onclick(com.pentabit.cvmaker.resumebuilder.utils.Constants.GALLERY)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxChooseProfile(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = ChooseEditProfileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.editProfile.setOnClickListener {
            param.onButtonClick(Constants.EDIT)
            dialogBuilder.dismiss()
        }
        binding.cancelProfile.setOnClickListener {
            dialogBuilder.dismiss()
        }
        binding.deleteProfile.setOnClickListener {
            param.onButtonClick(Constants.DELETE)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }


    fun alertboxChooseCreation(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = CreationdialogueBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.template.setOnClickListener {
            param.onButtonClick(Constants.TEMPLATE)
            dialogBuilder.dismiss()
        }
        binding.cancelProfile.setOnClickListener {
            dialogBuilder.dismiss()
        }
        binding.profile.setOnClickListener {
            param.onButtonClick(Constants.PROFILE)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxChooseDownload(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = ChoosedownloadLayoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.downJpg.setOnClickListener {
            param.onButtonClick(com.pentabit.cvmaker.resumebuilder.utils.Constants.JPG)

            dialogBuilder.dismiss()
        }
        binding.downPdf.setOnClickListener {
            param.onButtonClick(com.pentabit.cvmaker.resumebuilder.utils.Constants.PDF)

            dialogBuilder.dismiss()
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxChooseCreate(
        curentactivity: Activity,
        param: com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringValueDialogCallback
    ) {
        val binding = ChoosecreateprofileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.createProfile.setOnClickListener {
            param.onButtonClick(com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATE)
            dialogBuilder.dismiss()
        }
        binding.importProfile.setOnClickListener {
            param.onButtonClick(com.pentabit.cvmaker.resumebuilder.utils.Constants.IMPORT)
            dialogBuilder.dismiss()
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun showWheelDatePickerDialog(
        context: Activity,
        param: StringDialogCallback
    ) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val datePickerView: View = inflater.inflate(R.layout.wheelstyledatepicker, null)

        val dayPicker = datePickerView.findViewById<DatePicker>(R.id.day_picker)

        // Set the maximum date to the current date to hide future dates
        dayPicker.maxDate = System.currentTimeMillis()

        // Build the AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(datePickerView)
        builder.setTitle("Select Date")

        builder.setPositiveButton("OK") { dialog, which ->
            param.onButtonClick("${dayPicker.month + 1}/${dayPicker.dayOfMonth}/${dayPicker.year}") // Set the date in your EditText
        }

        builder.setNegativeButton("Cancel", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun showWheelDatePickerDialogDOB(
        context: Activity,
        param: StringDialogCallback
    ) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val datePickerView: View = inflater.inflate(R.layout.wheelstyledatepicker, null)

        val dayPicker = datePickerView.findViewById<DatePicker>(R.id.day_picker)

        // Set the maximum date to the current date
        val currentDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -15)
        }
        // Set the minimum date to 15 years ago
        val minDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -100)
        }
        dayPicker.minDate = minDate.timeInMillis
        dayPicker.maxDate = currentDate.timeInMillis
        // Set the initial date to 15 years ago
        val initialDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -15)
        }
        dayPicker.updateDate(
            initialDate.get(Calendar.YEAR),
            initialDate.get(Calendar.MONTH),
            initialDate.get(Calendar.DAY_OF_MONTH)
        )

        // Build the AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(datePickerView)
        builder.setTitle("Select Date")

        builder.setPositiveButton("OK") { dialog, which ->
            param.onButtonClick("${dayPicker.month + 1}/${dayPicker.dayOfMonth}/${dayPicker.year}") // Set the date in your EditText
        }

        builder.setNegativeButton("Cancel", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    fun alertboxImport(curentactivity: Activity, param: StringValueDialogCallback) {
        val binding = ImportProfileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.cancelBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        binding.importbtn.setOnClickListener {
            param.onButtonClick(Constants.YES)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }


    fun shareAppMethod(curentactivity: Activity) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Resume Maker")
        curentactivity.startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    fun link(url: String, context: Context) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context!!.startActivity(i)
    }
}