package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentSkillBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillFragment : AddDetailsBaseFragment<FragmentSkillBinding>(),
    AddSkillFragment.SkillUpdates {
    val skillAdapter = SingleStringAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<String>()
    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate
    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userSkills.isNullOrEmpty(), binding.popup)
            list = it.userSkills as ArrayList<String>
            setadapter()
        }
    }


    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty())
        {
            AppsKitSDKUtils.makeToast("please Add at least one skill")
            return false

        }else{
            callSaveApi()
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        getProfileDetail()

    }


    private fun getProfileDetail() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter() {
        skillAdapter.submitList(list)
        skillAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddSkillFragment(
                    list,
                    position, this
                )
            )
        }
        skillAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){

                deleteItemPopup(currentActivity(), "Do you want to delete this Skill record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    skillAdapter.submitList(list)
                                    skillAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            }else{
                AppsKitSDKUtils.makeToast("sorry at least one skill required")

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
                    this
                )
            )
        }
    }

    override fun skillUpdate(skillList: List<String>) {
        list = ArrayList(skillList)
        AppsKitSDKUtils.setVisibility(skillList.isEmpty(), binding.popup)
        skillAdapter.submitList(skillList)
    }
}