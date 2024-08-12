package com.example.resumemaker.views.fragments.coverletter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentCoverLetterSampleBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SampleAdapter


class CoverLetterSampleFragment : BaseFragment<FragmentCoverLetterSampleBinding>() {
    override val inflate: Inflate<FragmentCoverLetterSampleBinding>
        get() = FragmentCoverLetterSampleBinding::inflate

    override fun observeLiveData() {


    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.apply {
            textView.text="Samples"
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        val sampleAdapter=SampleAdapter(currentActivity(),Helper.getSuggestions())
        {
            val bundle=Bundle()
            bundle.putString(Constants.DATA,it.skillName)
            currentActivity().replaceChoiceFragment(R.id.nav_sample_detail_cover_letter,bundle)
        }
        binding.clSampleRecyclerview.adapter=sampleAdapter
    }
}
