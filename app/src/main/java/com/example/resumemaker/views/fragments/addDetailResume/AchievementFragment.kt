package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAchievementBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.AchievementAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>() {
    val achievementAdapter= AchievementAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override fun csnMoveForward(): Boolean {
        return false
    }

    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setAdapter(it.userAchievement)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(8)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            sharePref.writeBoolean(Constants.IS_RESUME,true)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = ResumePreviewFragment()

        }
        binding.addachievementbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = ResumePreviewFragment()
        }
    }

    private fun setAdapter(userAchievement: List<ProfileModelAddDetailResponse.UserAchievement>) {
        achievementAdapter.submitList(userAchievement)
        achievementAdapter.setOnEditItemClickCallback {
            callDeleteApi()
        }
        achievementAdapter.setOnItemDeleteClickCallback {
            sharePref.writeDataAchievement(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddAchievementsFRagment()
        }
        binding.recyclerviewAchievements.adapter = achievementAdapter
    }

    private fun callDeleteApi() {

    }

}
