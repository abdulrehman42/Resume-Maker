package com.example.resumemaker.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.FragmentLoginBinding
import com.example.resumemaker.utils.Constants
import com.google.android.material.elevation.SurfaceColors


class LoginActivity : BaseActivity() {
    lateinit var binding:FragmentLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.navy_blue))
        binding= FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginbtnlinearbtn.setOnClickListener{
            val intent=Intent(this,ProfileActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun attachViewMode() {

    }
}