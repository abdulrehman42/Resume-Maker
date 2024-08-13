package com.example.resumemaker.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.FragmentLoginBinding
import com.google.android.material.elevation.SurfaceColors


class LoginActivity : BaseActivity() {
    lateinit var binding:FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.navy_blue))
        binding= FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbtnlinearbtn.setOnClickListener{
            startActivity(Intent(this, BoardingScreen::class.java))
            finish()
        }

    }

    override fun attachViewMode() {

    }
}