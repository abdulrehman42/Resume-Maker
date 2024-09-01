package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>() {
    val interestAdapter = SingleStringAdapter()
    var list = ArrayList<String>()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    lateinit var tabhost: TabLayout

//    val interests = Array(30) { "" }


    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userInterests.isNullOrEmpty(), binding.popup)
            list = it.userInterests as ArrayList<String>
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
            AppsKitSDKUtils.makeToast("please add at least one interest")
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        AppsKitSDKAdsManager.showNative(
            currentActivity(), binding.bannerAdd, ""

        )
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter() {
        interestAdapter.submitList(list)
        interestAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddInterestFragment(
                    list,
                    item,
                    true,
                    position
                )
            )

        }
        interestAdapter.setOnItemDeleteClickCallback {
            if (list.size != 0) {
                list.removeAt(it)

            }
            //   setadapter()
            if (list.size != 0) {
                callSaveApi()
                apiCall()

            }
        }
        binding.recyclerviewInterest.adapter = interestAdapter
    }

    private fun callSaveApi() {
//        for (i in 0 until list.size)
//        {
//            interests[i]="-1__"+Helper.removeOneUnderscores(list[i])
//
//        }
        addDetailResumeVM.editInterest(
            InterestRequestModel(list)
        )
    }

    private fun onclick() {
        binding.addinterestbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddInterestFragment(
                    list,
                    null,
                    false,
                    0
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
}