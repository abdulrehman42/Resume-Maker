package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAchievementBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Achievement
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.AchievementAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>() {
    val achievementAdapter = AchievementAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<ProfileModelAddDetailResponse.UserAchievement>()


    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userAchievement.isNullOrEmpty(), binding.popup)
            list.clear()
            list = it.userAchievement as ArrayList<ProfileModelAddDetailResponse.UserAchievement>
            setAdapter()
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
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
            AppsKitSDKUtils.makeToast("please add at least one achievement")
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
        addDetailResumeVM.getProfileDetail()
    }

    private fun onclick() {
        binding.addachievementbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddAchievementsFRagment(
                    null,
                    list,
                    0,
                    false
                )
            )
        }
    }

    private fun setAdapter() {
        achievementAdapter.submitList(list)
        achievementAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddAchievementsFRagment(
                    item,
                    list,
                    position,
                    true
                )
            )
        }
        achievementAdapter.setOnItemDeleteClickCallback {
            if (list.size != 0) {
                list.removeAt(it)
            }
            // setAdapter()
            if (list.size != 0) {
                callSaveApi()
                apiCall()

            }
        }
        binding.recyclerviewAchievements.adapter = achievementAdapter
    }

    private fun callSaveApi() {
        var achievement = ArrayList<Achievement>()
        for (i in 0 until list.size) {
            achievement.add(
                Achievement(list[i].description, list[i].issueDate, list[0].title)
            )
        }
        val achievementRequest = AchievementRequest(achievements = achievement)

        addDetailResumeVM.editAchievement(
            achievementRequest
        )
    }

}
