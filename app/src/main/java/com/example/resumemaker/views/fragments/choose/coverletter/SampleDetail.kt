package com.example.resumemaker.views.fragments.choose.coverletter

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSampleDetailBinding


class SampleDetail : BaseFragment<FragmentSampleDetailBinding>()
{
    override val inflate: Inflate<FragmentSampleDetailBinding>
        get() = FragmentSampleDetailBinding::inflate

    override fun observeLiveData() {


    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {

        binding.includeTool.apply {
            textView.text="Sample"
            share.setImageResource(R.drawable.select_option)
        }
    }

}