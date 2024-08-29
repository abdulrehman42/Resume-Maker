package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAchievementBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Achievement
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Project
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATION_TIME
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseCreation
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.AchievementAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>() {
    val achievementAdapter= AchievementAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list=ArrayList<ProfileModelAddDetailResponse.UserAchievement>()


    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list= it.userAchievement as ArrayList<ProfileModelAddDetailResponse.UserAchievement>
            setAdapter()
        }

        addDetailResumeVM.loadingState.observe(this){
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
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

    private fun check(): Boolean {
        if(list.isNotEmpty()){
            return true
        }else{
            AppsKitSDKUtils.makeToast("please add at least one achievement")
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
        addDetailResumeVM.getProfileDetail()
    }

    private fun onclick() {
        binding.backbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).replaceByTabId(8)

        }
        binding.nextbtn.setOnClickListener {
            alertboxChooseCreation(requireActivity(),
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value == Constants.PROFILE) {
                            AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.VIEW_PROFILE,true)
                            currentActivity().finish()
                        } else {
                            val intent = Intent(currentActivity(), ChoiceTemplate::class.java)
                            intent.putExtra(IS_RESUME,true)
                            intent.putExtra(CREATION_TIME, true)
                            startActivity(intent)
                            currentActivity().finish()
                        }
                    }

                })

        }
        binding.addachievementbtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddAchievementsFRagment(null, list)
        }
    }

    private fun setAdapter() {
        achievementAdapter.submitList(list)
        achievementAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddAchievementsFRagment(it,list)
        }
        achievementAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setAdapter()
            if (list.size!=0)
            {
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
                Achievement(list[i].description, list[i].issueDate,list[0].title)
            )
        }
        val achievementRequest = AchievementRequest(achievements = achievement)

        addDetailResumeVM.editAchievement(
            achievementRequest
        )
    }

}
