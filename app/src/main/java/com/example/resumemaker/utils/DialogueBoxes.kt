package com.example.resumemaker.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.DatePicker
import android.widget.NumberPicker
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.databinding.AddRemoveBinding
import com.example.resumemaker.databinding.ChooseEditProfileBinding
import com.example.resumemaker.databinding.ChooseImageLayoutBinding
import com.example.resumemaker.databinding.ChoosecreateprofileBinding
import com.example.resumemaker.databinding.ChoosedownloadLayoutBinding
import com.example.resumemaker.databinding.DeleteAccountBinding
import com.example.resumemaker.databinding.ImportProfileBinding
import com.example.resumemaker.databinding.LogoutBinding
import com.example.resumemaker.databinding.RatingLayoutBinding
import com.example.resumemaker.databinding.TemplateSelectLayoutBinding
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
    fun alertbox(template: Any, currentActivity: Activity, param: DialogCallback) {
        val binding= TemplateSelectLayoutBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        Glide.with(currentActivity).load(Constants.BASE_MEDIA_URL+template).into(binding.templateimage)
        binding.linearbtn.setOnClickListener {
            param.onButtonClick(true)
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
    fun alertboxLogout(curentactivity: Activity) {
        val binding= LogoutBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.yesBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.noBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun alertboxDelete(curentactivity: Activity) {
        val binding= DeleteAccountBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.yesBtn.setOnClickListener {

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
            onclick(Constants.GALLERY)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun alertboxChooseProfile(curentactivity: Activity) {
        val binding= ChooseEditProfileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.editProfile.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.rename.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.duplicate.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.deleteProfile.setOnClickListener {

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
            param.onButtonClick(Constants.JPG)

            dialogBuilder.dismiss()
        }
        binding.downPdf.setOnClickListener {
            param.onButtonClick(Constants.PDF)

            dialogBuilder.dismiss()
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun alertboxChooseCreate(curentactivity: Activity,param: StringValueDialogCallback) {
        val binding= ChoosecreateprofileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.createProfile.setOnClickListener {
            param.onButtonClick(Constants.CREATE)
            dialogBuilder.dismiss()
        }
        binding.importProfile.setOnClickListener {
            param.onButtonClick(Constants.IMPORT)
            dialogBuilder.dismiss()
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
    fun showWheelDatePickerDialog(context: Activity,param: StringDialogCallback) {
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val datePickerView: View = inflater.inflate(R.layout.wheelstyledatepicker, null)

        val dayPicker = datePickerView.findViewById<DatePicker>(R.id.day_picker)


        // Build the AlertDialog
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setView(datePickerView)
        builder.setTitle("Select Date")

        builder.setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which ->
            param.onButtonClick("${dayPicker.month}/${dayPicker.dayOfMonth}/${dayPicker.year}") // Set the date in your EditText
        })

        builder.setNegativeButton("Cancel", null)

        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
    fun alertboxImport(curentactivity:Activity) {
        val binding= ImportProfileBinding.inflate(curentactivity.layoutInflater)
        val dialogBuilder = Dialog(curentactivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        binding.cancelBtn.setOnClickListener {

            dialogBuilder.dismiss()
        }
        binding.importbtn.setOnClickListener {
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