package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAIWritingBinding


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