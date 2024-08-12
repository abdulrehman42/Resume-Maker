package com.example.resumemaker.utils

import android.app.Activity
import android.app.Dialog
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.databinding.TemplateSelectLayoutBinding

object DialogueBoxes {
    interface DialogCallback {
        fun onButtonClick(isConfirmed: Boolean)
    }
    fun alertbox(template: Int, currentActivity: Activity, param: DialogCallback) {
        var check=false
        val binding= TemplateSelectLayoutBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        Glide.with(currentActivity).load(template).into(binding.templateimage)
        binding.linearbtn.setOnClickListener {
            param.onButtonClick(true)
            dialogBuilder.dismiss()
        }
        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
    }
}