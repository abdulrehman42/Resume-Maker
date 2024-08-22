package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInterestBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.request.addDetailResume.InterestRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.SingleStringAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>() {
    val interestAdapter=SingleStringAdapter()
    var list=ArrayList<String>()
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list= it.userInterests as ArrayList<String>
            setadapter(list)
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

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun setadapter(userInterests: List<String>) {
        interestAdapter.submitList(userInterests)
        interestAdapter.setOnEditItemClickCallback {
            sharePref.writeString(Constants.DATA,it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddInterestFragment()

        }
        interestAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            callSaveApi()
            apiCall()
        }
        binding.recyclerviewInterest.adapter = interestAdapter
    }

    private fun callSaveApi() {

        addDetailResumeVM.editInterest(
            sharePref.readString(Constants.PROFILE_ID).toString(),
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
            addDetailResumeVM.fragment.value = AddInterestFragment()
        }
    }


}