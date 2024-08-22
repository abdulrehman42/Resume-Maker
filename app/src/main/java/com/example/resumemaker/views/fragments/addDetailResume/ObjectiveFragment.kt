package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentObjectiveBinding
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.ObjSampleAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    lateinit var addDetailResumeVM:AddDetailResumeVM
    lateinit var objSampleAdapter: ObjSampleAdapter
    lateinit var tabhost:TabLayout
    var list=ArrayList<SampleResponseModel>()
    var num=0

    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(viewLifecycleOwner){
            list= it as ArrayList<SampleResponseModel>
            setAdapter(it)
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
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
            objSampleAdapter.updateMaxItemCount(list.size)
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
        addDetailResumeVM.editObjective(sharePref.readString(Constants.PROFILE_ID).toString(),
            SingleItemRequestModel(binding.objectiveTextInput.text.toString())
        )

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {

        objSampleAdapter= ObjSampleAdapter(currentActivity(),sampleResponseModels,{
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