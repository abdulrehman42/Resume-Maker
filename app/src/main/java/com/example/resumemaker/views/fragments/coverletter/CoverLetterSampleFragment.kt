package com.example.resumemaker.views.fragments.coverletter

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentCoverLetterSampleBinding
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.viewmodels.CoverLetterVM
import com.example.resumemaker.views.adapter.SampleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoverLetterSampleFragment : BaseFragment<FragmentCoverLetterSampleBinding>() {
    lateinit var coverLetterVM: CoverLetterVM
    override val inflate: Inflate<FragmentCoverLetterSampleBinding>
        get() = FragmentCoverLetterSampleBinding::inflate

    override fun observeLiveData() {
        coverLetterVM.getSamples.observe(currentActivity()){
            setAdapter(it)
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        coverLetterVM=ViewModelProvider(currentActivity())[CoverLetterVM::class.java]
        apiCall()
        binding.includeTool.apply {
            textView.text=getString(R.string.samples)
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {
        val sampleAdapter=SampleAdapter(currentActivity(),sampleResponseModels)
        {
            val bundle=Bundle()
            bundle.putString(Constants.TITLE_DATA,it.title)
            bundle.putString(Constants.DATA,it.body)
            currentActivity().replaceChoiceFragment(R.id.nav_sample_detail_cover_letter,bundle)
        }
        binding.clSampleRecyclerview.adapter=sampleAdapter
    }

    private fun apiCall() {
        coverLetterVM.getSample(Constants.COVER_LETTER)
    }
}
