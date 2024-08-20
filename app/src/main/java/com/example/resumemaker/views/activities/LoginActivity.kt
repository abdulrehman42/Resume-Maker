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
    var isResume=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = ContextCompat.getColor(this,R.color.navy_blue)
        binding= FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isResume=intent.getBooleanExtra(Constants.IS_RESUME,false)
        binding.loginbtnlinearbtn.setOnClickListener{
            if (isResume)
            {
                val intent =Intent(this,ProfileActivity::class.java)
                intent.putExtra(Constants.IS_RESUME,Constants.PROFILE)
                startActivity(intent)
                finish()
            }else{
                val intent=Intent(Intent(this,ChoiceTemplate::class.java))
                intent.putExtra(Constants.IS_RESUME,isResume)
                startActivity(intent)
                finish()
            }

        }

    }

    override fun attachViewMode() {

    }
}