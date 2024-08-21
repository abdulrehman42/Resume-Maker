package com.example.resumemaker.views.fragments.coverletter

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentCoverLetterSampleBinding
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.example.resumemaker.views.adapter.SampleAdapter
import com.example.resumemaker.views.fragments.addDetailResume.ResumePreviewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoverLetterSampleFragment : BaseFragment<FragmentCoverLetterSampleBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    override val inflate: Inflate<FragmentCoverLetterSampleBinding>
        get() = FragmentCoverLetterSampleBinding::inflate

    override fun observeLiveData() {
        templateViewModel.getSamples.observe(currentActivity()){
            setAdapter(it)
        }

    }

    override fun init(savedInstanceState: Bundle?) {
        templateViewModel=ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
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
            moveToFragment(bundle)
        }
        binding.clSampleRecyclerview.adapter=sampleAdapter
    }

    private fun apiCall() {
        templateViewModel.getSample(Constants.COVER_LETTER)
    }
    private fun moveToFragment(bundle: Bundle) {
        val fragment = AddDetailCoverLetterFragment()
        fragment.arguments = bundle
        currentActivity().supportFragmentManager.beginTransaction().setCustomAnimations(
            android.R.anim.fade_in,
            android.R.anim.fade_out,
            android.R.anim.fade_in,
            android.R.anim.fade_out
        )
            .replace(R.id.choice_template_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
