package com.example.resumemaker.views.fragments.coverletter

import android.annotation.SuppressLint
import android.os.Bundle
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSampleDetailBinding
import com.example.resumemaker.utils.Constants


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
            share.layoutParams.width=220
            share.layoutParams.height=250
            share.setImageResource(R.drawable.select_option)
            share.setOnClickListener {
                val bundle=Bundle()
                bundle.putString(Constants.DATA,binding.sampleText.text.toString())
                currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter,bundle)
            }
        }
    }

}