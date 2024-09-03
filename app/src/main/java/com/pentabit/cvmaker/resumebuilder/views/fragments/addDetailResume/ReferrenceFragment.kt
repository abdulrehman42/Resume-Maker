package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentReferrenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ReferenceAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReferrenceFragment : AddDetailsBaseFragment<FragmentReferrenceBinding>(),AddReferenceFragment.OnReferenceUpdate {
    val referenceAdapter = ReferenceAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var iscalled=false
    var list = ArrayList<ProfileModelAddDetailResponse.UserReference>()
    override val inflate: Inflate<FragmentReferrenceBinding>
        get() = FragmentReferrenceBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
        fetchProfileInfo()
    }

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userReferences.isNullOrEmpty(), binding.popup)
            populateAdapter(it.userReferences as ArrayList<ProfileModelAddDetailResponse.UserReference>)
            iscalled=true
        }
    }
    private fun fetchProfileInfo() {
            addDetailResumeVM.getProfileDetail()

    }

    private fun handleClicks() {
        binding.addreferrenebtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(AddReferenceFragment(list, null, this))
        }
    }

    private fun populateAdapter(referenceList: List<ProfileModelAddDetailResponse.UserReference>) {
        if (!iscalled)
        {
            saveInListWithRequiredFormat(referenceList)
            referenceAdapter.submitList(list)
        }

    }

    private fun saveInListWithRequiredFormat(referenceList: List<ProfileModelAddDetailResponse.UserReference>) {
        for (reference in referenceList) {
            val model = ProfileModelAddDetailResponse.UserReference(
                position = "1__" + Helper.removeOneUnderscores(reference.position),
                company = "1__" + Helper.removeOneUnderscores(reference.company),
                phone = reference.phone,
                email = reference.email,
                name = reference.name
            )
            list.add(model)
        }
    }

    private fun saveReferenceData() {
        addDetailResumeVM.editReference(
            list
        )
    }
    private fun handleAdapter() {
        referenceAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){
                deleteItemPopup(currentActivity(), "Do you want to delete this reference record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    referenceAdapter.submitList(list)
                                    referenceAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            }else{
                  AppsKitSDKUtils.makeToast("sorry at least one reference required")
            }

        }

        referenceAdapter.setOnEditItemClickCallback { _, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddReferenceFragment(list,position,this)
            )
        }

        binding.recyclerviewReference.adapter = referenceAdapter
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        saveReferenceData()
        return false
    }
    override fun referenceUpdate(listReference: List<ProfileModelAddDetailResponse.UserReference>) {
        list = ArrayList(listReference)
        AppsKitSDKUtils.setVisibility(listReference.isEmpty(), binding.popup)
        referenceAdapter.submitList(listReference)
    }


}