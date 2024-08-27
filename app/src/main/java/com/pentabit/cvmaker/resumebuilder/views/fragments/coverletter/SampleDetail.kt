package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.annotation.SuppressLint
import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentSampleDetailBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants


class SampleDetail : BaseFragment<FragmentSampleDetailBinding>()
{
    override val inflate: Inflate<FragmentSampleDetailBinding>
        get() = FragmentSampleDetailBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        val title=arguments?.getString(com.pentabit.cvmaker.resumebuilder.utils.Constants.TITLE_DATA)
        binding.includeTool.apply {
            textView.text=getString(R.string.samples)
            share.layoutParams.width=220
            share.layoutParams.height=250
            share.setImageResource(R.drawable.select_option)
            share.setOnClickListener {
                val bundle=Bundle()
                bundle.putString(Constants.TITLE_DATA,title)
                bundle.putString(Constants.DATA,binding.sampleText.text.toString())
                currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter,bundle)
            }
        }
    }

}