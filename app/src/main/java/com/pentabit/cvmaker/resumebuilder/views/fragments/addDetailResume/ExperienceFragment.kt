package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentExperienceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Experience
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ExperienceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
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
            list = it.userExperiences as ArrayList<ProfileModelAddDetailResponse.UserExperience>
            setadapter(list)
        }

        addDetailResumeVM.experienceResponse.observe(viewLifecycleOwner) {
            apiCall()
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

    private fun setadapter(userExperiences: List<ProfileModelAddDetailResponse.UserExperience>) {
        experienceAdapter.submitList(userExperiences)
        experienceAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddExperienceFragment(it)

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            callSaveApi()
            apiCall()

        }
        binding.recyclerviewExperience.adapter = experienceAdapter
    }

    private fun callSaveApi() {
        var experience = ArrayList<Experience>()
        for (i in 0 until list.size) {
            experience = listOf(
                Experience(
                    list[i].company,
                    list[i].description,
                    "fullTime",
                    list[i].endDate, list[i].startDate, list[i].title
                )
            ) as ArrayList<Experience>

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
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddExperienceFragment(null)

        }
    }


}