package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentExperienceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Experience
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ExperienceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment : AddDetailsBaseFragment<FragmentExperienceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<ProfileModelAddDetailResponse.UserExperience>()
    val experienceAdapter = ExperienceAdapter()
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(),binding.popup)

            list = it.userExperiences as ArrayList<ProfileModelAddDetailResponse.UserExperience>
            setadapter()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(

        )
    }
    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
    private fun setadapter() {
        experienceAdapter.submitList(list)

        experienceAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddExperienceFragment(it, list,false)

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            if (list.size!=0)
            {
                callSaveApi()
                apiCall()

            }

        }
        binding.recyclerviewExperience.adapter = experienceAdapter
    }

    private fun callSaveApi() {
        var experience = ArrayList<Experience>()
        for (i in 0 until list.size) {
            experience.add(
                Experience(
                    list[i].company,
                    list[i].description,
                    "fullTime",
                    list[i].endDate, list[i].startDate, list[i].title
                )
            )
        }
        val experienceRequest = ExperienceRequest(experiences = experience)
        addDetailResumeVM.editExperience(
            experienceRequest
        )
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(3)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()
        }
        binding.addexperiencebtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddExperienceFragment(null,list,true)

        }
    }


}