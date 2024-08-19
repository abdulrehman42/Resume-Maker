package com.example.resumemaker.views.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivitySplashScreenBinding
import com.google.android.material.elevation.SurfaceColors
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplachScreen : BaseActivity() {
    private lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MainScope().launch {
            delay(2000)
            startActivity(Intent(this@SplachScreen, BoardingScreen::class.java))
            finish()
        }
    }


    override fun attachViewMode() {
    }
}