package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Qualification
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.EducationAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationFragment : AddDetailsBaseFragment<FragmentEducationBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM

    var qualifications = ArrayList<Qualification>()
    var list = ArrayList<ProfileModelAddDetailResponse.UserQualification>()
    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(), binding.popup)
            list =
                it.userQualifications as ArrayList<ProfileModelAddDetailResponse.UserQualification>
            setAdapter()
        }

    }

    override fun csnMoveForward(): Boolean {
        return true
    }

   override fun onMoveNextClicked(): Boolean  {
       return true
    }

    private fun check(): Boolean {
        if (list.isNotEmpty()) {
            return true
        } else {
            AppsKitSDKUtils.makeToast("please add at least one education")
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        onclick()
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun onclick() {

        binding.addeducationbtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddEducation(null, list, false)
        }

    }

    private fun setAdapter() {
        educationAdapter = EducationAdapter(false, {
            addDetailResumeVM.fragment.value =
                AddEducation(it, list, true)
        }, {
            list.removeAt(it)
            if (list.size != 0) {
                callSaveApi()
                apiCall()

            }
        })
        educationAdapter.submitList(list)
        binding.recyclerviewEducation.adapter = educationAdapter
    }

    private fun callSaveApi() {
        for (i in 0 until list.size) {
            qualifications.add(
                Qualification(
                    degree = "1__" + Helper.removeOneUnderscores(list[i].degree),
                    endDate = list[i].endDate,
                    institute = "1__" + list[i].institute,
                    qualificationType = "degree",
                    startDate = list[i].startDate
                )
            )
        }
        val qualificationModelRequest = QualificationModelRequest(qualifications = qualifications)
        addDetailResumeVM.editQualification(
            qualificationModelRequest
        )
    }

}