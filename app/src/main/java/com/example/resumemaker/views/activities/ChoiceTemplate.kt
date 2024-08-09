package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityChoiceTemplateBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoiceTemplate : BaseActivity() {
    lateinit var binding: ActivityChoiceTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityChoiceTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun attachViewMode() {

    }

    @SuppressLint("ResourceAsColor")
    override fun onStart() {
        super.onStart()
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        window.statusBarColor=getResources().getColor(R.color.navy_blue)
    }

}