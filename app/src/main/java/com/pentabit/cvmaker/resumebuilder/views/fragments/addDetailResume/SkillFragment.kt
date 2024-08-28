package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentSkillBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillFragment : AddDetailsBaseFragment<FragmentSkillBinding>() {
    val skillAdapter= SingleStringAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list=ArrayList<String>()

    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(),binding.popup)
            list= it.userSkills as ArrayList<String>
            setadapter(list)
        }


        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }
    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        onclick()
        apiCall()

    }


    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter(userSkills: List<String>) {
        skillAdapter.submitList(userSkills)
        skillAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddSkillFragment(userSkills,it)
        }
        skillAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setadapter(list)
            if (list.size!=0)
            {
                callSaveApi()
                apiCall()

            }
        }

        binding.recyclerviewSkill.apply {
            adapter = skillAdapter
        }
    }

    private fun callSaveApi() {
        addDetailResumeVM.editSkill(
            SkillRequestModel(list)
        )
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(2)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(4)!!.select()

        }
        binding.addskill.setOnClickListener {
            addDetailResumeVM.fragment.value = AddSkillFragment(list, null)
        }
    }
}