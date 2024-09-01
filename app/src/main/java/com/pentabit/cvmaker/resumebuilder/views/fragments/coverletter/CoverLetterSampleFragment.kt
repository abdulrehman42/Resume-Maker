package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentCoverLetterSampleBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.SampleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoverLetterSampleFragment : BaseFragment<FragmentCoverLetterSampleBinding>() {
    lateinit var templateViewModel: TemplateViewModel
    val screenId = ScreenIDs.COVER_LETTER_SAMPLES
    override val inflate: Inflate<FragmentCoverLetterSampleBinding>
        get() = FragmentCoverLetterSampleBinding::inflate

    override fun observeLiveData() {
        templateViewModel.getSamples.observe(currentActivity()) {
            setAdapter(it)
        }

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun init(savedInstanceState: Bundle?) {
        templateViewModel = ViewModelProvider(currentActivity())[TemplateViewModel::class.java]
        apiCall()
        binding.includeTool.apply {
            textView.text = getString(R.string.samples)
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {
        val sampleAdapter = SampleAdapter(currentActivity(), sampleResponseModels)
        {
            val bundle = Bundle()
            bundle.putString(Constants.TITLE_DATA, it.title)
            bundle.putString(Constants.DATA, it.body)
            moveToFragment(bundle)
        }
        binding.clSampleRecyclerview.adapter = sampleAdapter
    }

    private fun apiCall() {
        templateViewModel.getSample(com.pentabit.cvmaker.resumebuilder.utils.Constants.COVER_LETTER)
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
