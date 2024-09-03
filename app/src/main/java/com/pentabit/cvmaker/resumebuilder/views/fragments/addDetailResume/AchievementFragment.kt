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
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.AchievementAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>(),
    AddAchievementsFRagment.OnAchievemntUpdate {
    val achievementAdapter = AchievementAdapter()
    var isCalled=false
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<ProfileModelAddDetailResponse.UserAchievement>()


    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
        fetchProfileData()
    }


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userAchievement.isNullOrEmpty(), binding.popup)
            populateAdapter(it.userAchievement as ArrayList<ProfileModelAddDetailResponse.UserAchievement>)
            isCalled=true
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }


    override fun onMoveNextClicked(): Boolean {
        saveEducationData()
        return false
    }

    private fun handleClicks() {
        binding.addachievementbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddAchievementsFRagment(
                    list,
                    null,
                    this
                )
            )
        }
    }

    private fun handleAdapter() {
        achievementAdapter.setOnItemDeleteClickCallback {
            deleteItemPopup(currentActivity(), "Do you want to delete this achievement record",
                object : DialogueBoxes.DialogCallback {
                    override fun onButtonClick(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            if (list.isNotEmpty()) {
                                list.removeAt(it)
                                achievementAdapter.submitList(list)
                                achievementAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                })
        }

        achievementAdapter.setOnEditItemClickCallback { _, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddAchievementsFRagment(
                    list,
                    position,
                    this
                )
            )
        }

        achievementAdapter.submitList(list)
        binding.recyclerviewAchievements.adapter=achievementAdapter

    }

    private fun populateAdapter(achievementList: ArrayList<ProfileModelAddDetailResponse.UserAchievement>) {
       if (!isCalled)
       {
           saveInListWithRequiredFormat(achievementList)
           achievementAdapter.submitList(list)
       }

    }

    private fun saveInListWithRequiredFormat(achievementList: List<ProfileModelAddDetailResponse.UserAchievement>) {
        for (achievement in achievementList) {
            val model = ProfileModelAddDetailResponse.UserAchievement(
                title = "1__" + Helper.removeOneUnderscores(achievement.title),
                description = achievement.description,
                issueDate = achievement.issueDate,
            )
            list.add(model)
        }
    }

    private fun saveEducationData() {
        addDetailResumeVM.editAchievement(
            list
        )
    }

    private fun fetchProfileData() {
        addDetailResumeVM.getProfileDetail()
    }

    override fun onAchievemnet(listAchievement: ArrayList<ProfileModelAddDetailResponse.UserAchievement>) {
        list = ArrayList(listAchievement)
        AppsKitSDKUtils.setVisibility(listAchievement.isEmpty(), binding.popup)
        achievementAdapter.submitList(listAchievement)
        achievementAdapter.notifyDataSetChanged()
    }

}
