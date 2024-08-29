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
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATION_TIME
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseCreation
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>() {
    val interestAdapter= SingleStringAdapter()
    var list=ArrayList<String>()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    lateinit var tabhost: TabLayout
    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(),binding.popup)
            list= it.userInterests as ArrayList<String>
            setadapter()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    private fun check(): Boolean {
        if(list.isNotEmpty()){
            return true
        }else{
            AppsKitSDKUtils.makeToast("please add at least one interest")
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showNative(currentActivity(),binding.bannerAdd,""

        )
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter() {
        interestAdapter.submitList(list)
        interestAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddInterestFragment(list,it,true)

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
        binding.backbtn.setOnClickListener {
            if (tabhost.tabCount >= 7) {
                (requireActivity() as AddDetailResume).replaceByTabId(6)
            }
        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 7) {
                (requireActivity() as AddDetailResume).replaceByTabId(7)
            }else{
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
        }
        binding.addinterestbtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddInterestFragment(list,null,false)
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
}