package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
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
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ObjectiveFragment : AddDetailsBaseFragment<FragmentObjectiveBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    lateinit var objSampleAdapter: ObjSampleAdapter
    lateinit var tabhost: TabLayout
    var list = ArrayList<SampleResponseModel>()
    private var isEditProfile = false

    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.getSamples.observe(this) {
            list = it as ArrayList<SampleResponseModel>
            setAdapter(it)
        }

        addDetailResumeVM.dataResponse.observe(this) {
            binding.objectiveTextInput.setText(it.objective)
        }

        addDetailResumeVM.data.observe(this) {
            binding.objectiveTextInput.setText(it.toString())
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
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
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        isEditProfile = requireActivity().intent.getBooleanExtra(Constants.IS_EDIT, false)
        if (isEditProfile) {
            addDetailResumeVM.getProfileDetail()
        }

        callAPi()
        onclick()

    }

    private fun callAPi() {
        addDetailResumeVM.getSample("objective")
    }

    private fun onclick() {

        binding.viewall.setOnClickListener {
            addDetailResumeVM.fragment.value = ObjectiveSample(list)
        }

        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(0)!!.select()
        }
        binding.nextbtn.setOnClickListener {
            if (isConditionMet()) {
                callEditAPi()
            } else {
                AppsKitSDKUtils.makeToast(getString(R.string.field_missing_error))
            }
        }
    }

    private fun callEditAPi() {
        addDetailResumeVM.editObjective(
            SingleItemRequestModel(binding.objectiveTextInput.text.toString())
        )

    }

    private fun setAdapter(sampleResponseModels: List<SampleResponseModel>) {

        objSampleAdapter = ObjSampleAdapter(currentActivity(), sampleResponseModels) {
            binding.objectiveTextInput.setText(it)
        }
        binding.sampleRecyclerview.adapter = objSampleAdapter
    }

    fun isConditionMet(): Boolean {
        return !binding.objectiveTextInput.text.toString().isNullOrEmpty()
    }


}