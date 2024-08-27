package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>() {
    val interestAdapter= SingleStringAdapter()
    var list=ArrayList<String>()
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list= it.userInterests as ArrayList<String>
            setadapter(list)
        }
        addDetailResumeVM.interestResponse.observe(viewLifecycleOwner){
            apiCall()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it.loader)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
            currentActivity().showToast(it.msg)
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showNative(currentActivity(),binding.bannerAdd,""

        );
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter(userInterests: List<String>) {
        interestAdapter.submitList(userInterests)
        interestAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddInterestFragment(userInterests,it)

        }
        interestAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            if (list.size!=0)
            {
                callSaveApi()
                apiCall()

            }
        }
        binding.recyclerviewInterest.adapter = interestAdapter
    }

    private fun callSaveApi() {

        addDetailResumeVM.editInterest(
            InterestRequestModel(list)
        )
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(6)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 7) {
                tabhost.getTabAt(7)!!.select()
            }else{
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()

            }
        }
        binding.addinterestbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddInterestFragment(null,null)
        }
    }


}