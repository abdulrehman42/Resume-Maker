package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentSkillBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillFragment : AddDetailsBaseFragment<FragmentSkillBinding>() {
    val skillAdapter = SingleStringAdapter()

    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<String>()

    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userSkills.isNullOrEmpty(), binding.popup)
            list = it.userSkills as ArrayList<String>
            setadapter(list)
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
            AppsKitSDKUtils.makeToast("please add at least one skill")
            return false
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
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
        skillAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddSkillFragment(
                    userSkills,
                    item,
                    position,
                    true
                )
            )
        }
        skillAdapter.setOnItemDeleteClickCallback {
            if (list.size != 0) {
                list.removeAt(it)
            }
            //   setadapter(list)
            if (list.size != 0) {
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
        binding.addskill.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddSkillFragment(
                    list,
                    null,
                    0,
                    false
                )
            )
        }
    }
}