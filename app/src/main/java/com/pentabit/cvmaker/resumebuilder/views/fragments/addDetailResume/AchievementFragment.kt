package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAchievementBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.AchievementAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AchievementFragment : com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment<FragmentAchievementBinding>() {
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
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString())
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(8)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            sharePref.writeBoolean(com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME,true)
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
