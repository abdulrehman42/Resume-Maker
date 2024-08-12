package com.example.resumemaker.utils

import android.app.Activity
import android.app.Dialog
import com.bumptech.glide.Glide
import com.example.resumemaker.R
import com.example.resumemaker.databinding.TemplateSelectLayoutBinding

object DialogueBoxes {
    fun alertbox(template:Int,currentActivity:Activity):Boolean {
        val binding= TemplateSelectLayoutBinding.inflate(currentActivity.layoutInflater)
        val dialogBuilder = Dialog(currentActivity, R.style.Custom_Dialog)
        dialogBuilder.setContentView(binding.root)
        Glide.with(currentActivity).load(template).into(binding.templateimage)
        binding.textView8.setOnClickListener {
            return@setOnClickListener
        }

        dialogBuilder.window?.setBackgroundDrawableResource(R.drawable.alertdialogue_radius)
        dialogBuilder.setCancelable(true)
        dialogBuilder.show()
        return false
    }
}