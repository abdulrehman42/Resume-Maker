package com.pentabit.cvmaker.resumebuilder.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.DatePicker
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.databinding.AddRemoveBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChooseEditProfileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChooseImageLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChoosecreateprofileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ChoosedownloadLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.DeleteAccountBinding
import com.pentabit.cvmaker.resumebuilder.databinding.ImportProfileBinding
import com.pentabit.cvmaker.resumebuilder.databinding.LogoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.PreviewdownloadBinding
import com.pentabit.cvmaker.resumebuilder.databinding.RatingLayoutBinding
import com.pentabit.cvmaker.resumebuilder.databinding.TemplateSelectLayoutBinding


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
    fun alertbox(template: Any, currentActivity: Activity, param: com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.DialogCallback) {
        val binding= TemplateSelectLayoutBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        Glide.with(currentActivity).load(com.pentabit.cvmaker.resumebuilder.utils.Constants.BASE_MEDIA_URL +template).into(binding.templateimage)
        binding.linearbtn.setOnClickListener {
            param.onButtonClick(true)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }

    fun alertboxPdf(template: String, currentActivity: Activity) {
        val binding= PreviewdownloadBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.templateimage.webViewClient = WebViewClient()
            binding.templateimage.loadDataWithBaseURL(
                Constants.BASE_URL_PRODUCTION,
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
    fun alertboxRate(curentactivity:Activity) {
        val binding= RatingLayoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.rateBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun alertboxPurchase(curentactivity:Activity) {
        val binding= AddRemoveBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.removeAddBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.maybeLaterBtn.setOnClickListener {
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun alertboxLogout(curentactivity: Activity,param: StringValueDialogCallback) {
        val binding= LogoutBinding.inflate(curentactivity.layoutInflater)
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
    fun alertboxDelete(curentactivity: Activity,param: StringValueDialogCallback) {
        val binding= DeleteAccountBinding.inflate(curentactivity.layoutInflater)
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
    fun alertboxChooseImage(curentactivity: Activity,onclick:(String)->Unit) {
        val binding= ChooseImageLayoutBinding.inflate(curentactivity.layoutInflater)
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
    fun alertboxChooseProfile(curentactivity: Activity,param: StringValueDialogCallback) {
        val binding= ChooseEditProfileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.editProfile.setOnClickListener {
            param.onButtonClick(Constants.EDIT)
            dialogBuilder.dismiss()
        }
        binding.rename.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.duplicate.setOnClickListener {

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
    fun alertboxChooseDownload(curentactivity: Activity,param: StringValueDialogCallback) {
        val binding= ChoosedownloadLayoutBinding.inflate(curentactivity.layoutInflater)
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
    fun alertboxChooseCreate(curentactivity: Activity,param: com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringValueDialogCallback) {
        val binding= ChoosecreateprofileBinding.inflate(curentactivity.layoutInflater)
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
    fun alertboxImport(curentactivity:Activity,param: StringValueDialogCallback) {
        val binding= ImportProfileBinding.inflate(curentactivity.layoutInflater)
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


    fun shareAppMethod(curentactivity: Activity){
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Resume Maker")
        curentactivity.startActivity(Intent.createChooser(shareIntent, "choose one"))
    }

    fun link(url: String,context: Context) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(url)
        context!!.startActivity(i)
    }
}