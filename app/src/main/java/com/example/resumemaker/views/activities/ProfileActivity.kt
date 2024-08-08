package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity

class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_profile)

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