package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentObjectiveBinding
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.ObjectiveModelRequest
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.ObjSampleAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    lateinit var addDetailResumeVM:AddDetailResumeVM
    lateinit var objSampleAdapter: ObjSampleAdapter
    lateinit var tabhost:TabLayout
    var num=0

    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(viewLifecycleOwner){
            setAdapter(it)
        }
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner){
            tabhost.getTabAt(2)!!.select()
        }
    }

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(this)[AddDetailResumeVM::class.java]
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        callAPi()
        onclick()

    }

    private fun callAPi() {
        addDetailResumeVM.getSample("objective")
    }

    private fun onclick() {

        binding.viewall.setOnClickListener {
            objSampleAdapter.updateMaxItemCount(Helper.getSampleList().size)
        }
        binding.viewless.setOnClickListener {
            objSampleAdapter.updateMaxItemCount(2)
            binding.viewall.setOnClickListener {
                num += 2
                objSampleAdapter.updateMaxItemCount(num)

                //setAdapter(it)
            }
            binding.viewless.setOnClickListener {
                num -= 2
                objSampleAdapter.updateMaxItemCount(num)
                // setAdapter(it)
            }


        }
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(0)!!.select()
        }
        binding.nextbtn.setOnClickListener {
            if (isConditionMet())
            {
                callEditAPi()
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))
            }
        }
    }

    private fun callEditAPi() {
        addDetailResumeVM.editObjective("7422",ObjectiveModelRequest(binding.objectiveTextInput.text.toString()))

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {

        objSampleAdapter=ObjSampleAdapter(currentActivity(),sampleResponseModels,{
            binding.objectiveTextInput.setText(it)
        },{
            if (it) {
                binding.viewall.isGone=true
                binding.viewless.isGone=false
            }else{
                binding.viewall.isGone=false
                binding.viewless.isGone=true
            }
        })
        binding.sampleRecyclerview.adapter=objSampleAdapter
    }
    fun isConditionMet(): Boolean {
        return !binding.objectiveTextInput.text.toString().isNullOrEmpty()
    }
}