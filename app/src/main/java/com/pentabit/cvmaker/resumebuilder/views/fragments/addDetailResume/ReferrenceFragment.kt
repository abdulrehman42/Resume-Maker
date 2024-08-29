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
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentReferrenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATION_TIME
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseCreation
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseProfile
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.activities.ProfileActivity
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
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(),binding.popup)
            list = it.userReferences as ArrayList<ProfileModelAddDetailResponse.UserReference>
            setadapter(list)
        }

        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }

    override fun csnMoveForward(): Boolean {
        return check()
    }

    private fun check(): Boolean {
        if(list.isNotEmpty()){
            return true
        }else{
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
        referenceAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddReferenceFragment(it, list)

        }
        referenceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setadapter(list)
            if (list.size!=0)
            {
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
        binding.backbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).replaceByTabId(5)

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 7) {
                (requireActivity() as AddDetailResume).replaceByTabId(6)

            } else {
                alertboxChooseCreation(requireActivity(),
                    object : DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.PROFILE) {
                                AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.VIEW_PROFILE,true)
                                startActivity(Intent(requireActivity(),ProfileActivity::class.java))
                                requireActivity().finish()
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
        binding.addreferrenebtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddReferenceFragment(null,list)
        }
    }
}