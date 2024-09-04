package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
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
class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>(),AddInterestFragment.OnInterestUpdate {
    val interestAdapter = SingleStringAdapter()
    var list = ArrayList<String>()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()


    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userInterests.isNullOrEmpty(), binding.popup)
            list = it.userInterests as ArrayList<String>
            interestAdapter.submitList(list)
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty())
        {
            AppsKitSDKUtils.makeToast("please Add at least one interest")
            return false

        }else{
            savedInterestApi()
            return false
        }
    }

    private fun savedInterestApi() {
        addDetailResumeVM.editInterest(
            InterestRequestModel(list)
        )
    }

    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
        fetchProfileInfo()
    }

    private fun fetchProfileInfo() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun handleClicks() {
        binding.addinterestbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(AddInterestFragment(list, null, this))
        }
    }

    private fun handleAdapter() {
        interestAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){
                deleteItemPopup(currentActivity(), "Do you want to delete this interest record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    interestAdapter.submitList(list)
                                    interestAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            }else{
                AppsKitSDKUtils.makeToast("sorry at least one interest required")

            }

        }

        interestAdapter.setOnEditItemClickCallback { _, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddInterestFragment(
                    list, position, this
                )
            )
        }

        binding.recyclerviewInterest.adapter = interestAdapter
    }


    override fun onInterest(interesList: List<String>) {
        list = ArrayList(interesList)
        AppsKitSDKUtils.setVisibility(interesList.isEmpty(), binding.popup)
        interestAdapter.submitList(interesList)
    }
}