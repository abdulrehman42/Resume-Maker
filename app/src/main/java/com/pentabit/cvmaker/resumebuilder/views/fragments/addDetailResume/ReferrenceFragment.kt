package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentReferrenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ReferenceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferrenceFragment : AddDetailsBaseFragment<FragmentReferrenceBinding>() {
    val referenceAdapter = ReferenceAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    lateinit var tabhost: TabLayout
    var list = ArrayList<ProfileModelAddDetailResponse.UserReference>()
    override val inflate: Inflate<FragmentReferrenceBinding>
        get() = FragmentReferrenceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userReferences.isEmpty(), binding.popup)
            list = it.userReferences as ArrayList<ProfileModelAddDetailResponse.UserReference>
            setadapter(list)
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

    override fun onMoveNextClicked(): Boolean {
        return true
    }

    private fun check(): Boolean {
        if (list.isNotEmpty()) {
            return true
        } else {
            AppsKitSDKUtils.makeToast("please add at least one reference")
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        tabhost = currentActivity().findViewById(R.id.tab_layout_adddetail)!!

        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(IS_RESUME, true)
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter(userReferences: List<ProfileModelAddDetailResponse.UserReference>) {
        referenceAdapter.submitList(userReferences)
        referenceAdapter.setOnEditItemClickCallback {item,position->
            addDetailResumeVM.fragment.value = AddReferenceFragment(item, list,true,position)

        }
        referenceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setadapter(list)
            if (list.size != 0) {
                callSaveApi()
                apiCall()

            }
        }
        binding.recyclerviewReference.adapter = referenceAdapter
    }

    private fun callSaveApi() {
        var reference = ArrayList<ReferenceRequest.Reference>()
        for (i in 0 until list.size) {
            reference.add(
                ReferenceRequest.Reference(
                    list[i].company,
                    list[i].email,
                    list[i].name,
                    list[i].phone,
                    list[i].position
                )
            )
        }
        val referenceRequest = ReferenceRequest(references = reference)

        addDetailResumeVM.editReference(
            referenceRequest
        )
    }

    private fun onclick() {
        binding.addreferrenebtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddReferenceFragment(null, list, false, 0)
        }
    }
}