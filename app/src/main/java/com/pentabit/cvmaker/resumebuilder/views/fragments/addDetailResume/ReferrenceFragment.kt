package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentReferrenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ReferenceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferrenceFragment : AddDetailsBaseFragment<FragmentReferrenceBinding>() {
    val referenceAdapter = ReferenceAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<ProfileModelAddDetailResponse.UserReference>()
    override val inflate: Inflate<FragmentReferrenceBinding>
        get() = FragmentReferrenceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list = it.userReferences as ArrayList<ProfileModelAddDetailResponse.UserReference>
            setadapter(list)
        }
        addDetailResumeVM.referenceResponse.observe(viewLifecycleOwner){
            apiCall()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        sharePref.writeBoolean(com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME, true)
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString())
    }

    private fun setadapter(userReferences: List<ProfileModelAddDetailResponse.UserReference>) {
        referenceAdapter.submitList(userReferences)
        referenceAdapter.setOnEditItemClickCallback {
            sharePref.writeDataReference(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddReferenceFragment()

        }
        referenceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setadapter(list)
            callSaveApi()
            apiCall()
        }
        binding.recyclerviewReference.adapter = referenceAdapter
    }

    private fun callSaveApi() {
        var reference = ArrayList<ReferenceRequest.Reference>()
        for (i in 0 until list.size) {
            reference = listOf(
                ReferenceRequest.Reference(
                    list[i].company,
                    list[i].email,
                    list[i].name,
                    list[i].phone,
                    list[i].position
                )
            ) as ArrayList<ReferenceRequest.Reference>
        }
        val referenceRequest = ReferenceRequest(references = reference)

        addDetailResumeVM.editReference(
            sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString(), referenceRequest
        )
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 7) {
                tabhost.getTabAt(6)!!.select()
            } else {
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()
            }
        }
        binding.addreferrenebtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddReferenceFragment()
        }
    }
}