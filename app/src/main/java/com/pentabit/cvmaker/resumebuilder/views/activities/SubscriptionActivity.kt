package com.pentabit.cvmaker.resumebuilder.views.activities

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySubscriptionBinding


class SubscriptionActivity : BaseActivity() {
    lateinit var binding:ActivitySubscriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.term.paintFlags = binding.term.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.cancelBtn.setOnClickListener {
            finish()
        }
        binding.purchaseConstraint.setOnClickListener {
            finish()
        }

    }

    override fun attachViewMode() {

    }
}