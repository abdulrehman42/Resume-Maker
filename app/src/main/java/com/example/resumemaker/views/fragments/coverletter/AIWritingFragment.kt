package com.example.resumemaker.views.fragments.coverletter

import android.os.Bundle
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAIWritingBinding


class AIWritingFragment : BaseFragment<FragmentAIWritingBinding>()
{
    override val inflate: Inflate<FragmentAIWritingBinding>
        get() = FragmentAIWritingBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.apply {
            textView.text="AI Writing"
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        binding.shortText.setOnClickListener {
            binding.shortText.setBackgroundResource(R.drawable.greenradiusripple)
            binding.mediumText.setBackgroundResource(R.drawable.greyrippleeffect)
            binding.longText.setBackgroundResource(R.drawable.greyrippleeffect)

        }
        binding.mediumText.setOnClickListener {
            binding.shortText.setBackgroundResource(R.drawable.greenradiusripple)
            binding.mediumText.setBackgroundResource(R.drawable.greenradiusripple)
            binding.longText.setBackgroundResource(R.drawable.greyrippleeffect)

        }
        binding.longText.setOnClickListener {
            binding.shortText.setBackgroundResource(R.drawable.greyrippleeffect)
            binding.mediumText.setBackgroundResource(R.drawable.greyrippleeffect)
            binding.longText.setBackgroundResource(R.drawable.greenradiusripple)

        }
    }

}