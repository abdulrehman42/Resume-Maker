package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveSampleBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.SampleAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ObjSampleAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter.AddDetailCoverLetterFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveSample : BaseFragment<FragmentObjectiveSampleBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentObjectiveSampleBinding>
        get() = FragmentObjectiveSampleBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(viewLifecycleOwner) {
            setAdapter(it)
        }
    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {
        val objSampleAdapter = ObjSampleAdapter(currentActivity(), sampleResponseModels, {
            addDetailResumeVM.data.value=it
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }, {
        })
        objSampleAdapter.updateMaxItemCount(sampleResponseModels.size)
        binding.objSampleRecyclerview.adapter = objSampleAdapter
    }




    private fun apiCall() {
        addDetailResumeVM.getSample(Constants.OBJECTIVE)
    }
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        apiCall()
        binding.includeTool.apply {
            textView.text=getString(R.string.samples)
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }



}