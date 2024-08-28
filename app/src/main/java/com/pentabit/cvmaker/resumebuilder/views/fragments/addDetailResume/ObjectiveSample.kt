package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveSampleBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ObjSampleAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveSample(val list: ArrayList<SampleResponseModel>?) :
    BaseFragment<FragmentObjectiveSampleBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentObjectiveSampleBinding>
        get() = FragmentObjectiveSampleBinding::inflate

    override fun observeLiveData() {

    }

    private fun setAdapter() {
        val objSampleAdapter = list?.let {
            ObjSampleAdapter(currentActivity(), it) {
                addDetailResumeVM.data.value = it
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        list?.let { objSampleAdapter?.updateMaxItemCount(it.size) }
        binding.objSampleRecyclerview.adapter = objSampleAdapter
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.apply {
            textView.text = getString(R.string.samples)
            setAdapter()
            backbtn.setOnClickListener {
                currentActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }


}