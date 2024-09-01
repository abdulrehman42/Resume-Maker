package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentExperienceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Experience
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ExperienceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment : AddDetailsBaseFragment<FragmentExperienceBinding>() {

    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<ProfileModelAddDetailResponse.UserExperience>()
    val experienceAdapter = ExperienceAdapter()
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userExperiences.isNullOrEmpty(), binding.popup)

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

        experienceAdapter.setOnEditItemClickCallback {item,position->

            (requireActivity() as AddDetailResume).openFragment( AddExperienceFragment(item, list, true,position))

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            if (list.size!=0)
            {
                list.removeAt(it)
            }
            setadapter()
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
                    "1__"+Helper.removeOneUnderscores(list[i].company),
                    list[i].description,
                    "fullTime",
                    list[i].endDate, list[i].startDate, "1__"+Helper.removeOneUnderscores(list[i].title)
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
            (requireActivity() as AddDetailResume).openFragment(
                AddExperienceFragment(
                    null,
                    list,
                    false,
                    0
                ))

        }
    }


}