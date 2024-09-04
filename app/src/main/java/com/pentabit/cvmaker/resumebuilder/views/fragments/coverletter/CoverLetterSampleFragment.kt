package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnCoverLetterSampleSelected
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentCoverLetterSampleBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.SampleAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoverLetterSampleFragment : BaseFragment<FragmentCoverLetterSampleBinding>() {
    private val templateViewModel: TemplateViewModel by activityViewModels()
    val screenId = ScreenIDs.COVER_LETTER_SAMPLES
    lateinit var callback: OnCoverLetterSampleSelected
    override val inflate: Inflate<FragmentCoverLetterSampleBinding>
        get() = FragmentCoverLetterSampleBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        handleAds()
        apiCall()
        binding.includeTool.apply {
            textView.text = getString(R.string.samples)
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            requireActivity(),
            binding.banner,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }


    override fun observeLiveData() {
        templateViewModel.getSamples.observe(currentActivity()) {
            setAdapter(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCoverLetterSampleSelected)
            callback = context
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {
        val sampleAdapter = SampleAdapter(currentActivity(), sampleResponseModels)
        {
            moveToFragment(it.title, it.body)
        }
        binding.clSampleRecyclerview.adapter = sampleAdapter
    }

    private fun apiCall() {
        templateViewModel.getSample(com.pentabit.cvmaker.resumebuilder.utils.Constants.COVER_LETTER)
    }

    private fun moveToFragment(title: String, body: String) {
        callback.onCoverLetterSampleSelected(title, body)
    }
}
