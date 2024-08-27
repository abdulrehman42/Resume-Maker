package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentObjectiveBinding
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SingleItemRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ObjSampleAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter.CoverLetterSampleFragment
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    lateinit var objSampleAdapter: ObjSampleAdapter
    lateinit var tabhost: TabLayout
    var list = ArrayList<SampleResponseModel>()
    var num = 0

    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(viewLifecycleOwner) {
            list = it as ArrayList<SampleResponseModel>
            setAdapter(it)
        }

        addDetailResumeVM.data.observe(currentActivity()) {
            binding.objectiveTextInput.setText(it.toString())
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            if (it.loader) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
            if (!it.msg.isNullOrBlank()) {
                currentActivity().showToast(it.msg)
            }
        }
        addDetailResumeVM.objectiveResponse.observe(viewLifecycleOwner) {
            tabhost.getTabAt(2)!!.select()
        }
    }

    override fun csnMoveForward(): Boolean {
        return isConditionMet()
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )

        callAPi()

        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        onclick()

    }

    private fun callAPi() {
        addDetailResumeVM.getSample("objective")
    }

    private fun onclick() {

        binding.viewall.setOnClickListener {
            //objSampleAdapter.updateMaxItemCount(list.size)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = ObjectiveSample()
        }

        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(0)!!.select()
        }
        binding.nextbtn.setOnClickListener {
            if (isConditionMet()) {
                callEditAPi()
            } else {
                currentActivity().showToast(getString(R.string.field_missing_error))
            }
        }
    }

    private fun callEditAPi() {
        addDetailResumeVM.editObjective(
            SingleItemRequestModel(binding.objectiveTextInput.text.toString())
        )

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {

        objSampleAdapter = ObjSampleAdapter(currentActivity(), sampleResponseModels, {
            binding.objectiveTextInput.setText(it)
        }, {
            /*if (it) {
                binding.viewall.isGone = true
                binding.viewLess.isGone = false
            } else {
                binding.viewall.isGone = false
                binding.viewLess.isGone = true
            }*/
        })
        binding.sampleRecyclerview.adapter = objSampleAdapter
    }

    fun isConditionMet(): Boolean {
        return !binding.objectiveTextInput.text.toString().isNullOrEmpty()
    }


}