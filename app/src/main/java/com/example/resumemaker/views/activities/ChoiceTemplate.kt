package com.example.resumemaker.views.activities

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityChoiceTemplateBinding

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


}