package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(), binding.popup)

            list = it.userExperiences as ArrayList<ProfileModelAddDetailResponse.UserExperience>
            setadapter()
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        return true
    }

    private fun check(): Boolean {
        if (list.isNotEmpty()) {
            return true
        } else {
            AppsKitSDKUtils.makeToast("please add at least one experience")
            return false
        }
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
            addDetailResumeVM.fragment.value = AddExperienceFragment(it, list, false)

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            if (list.size != 0) {
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

        binding.addexperiencebtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddExperienceFragment(null, list, true)

        }
    }


}