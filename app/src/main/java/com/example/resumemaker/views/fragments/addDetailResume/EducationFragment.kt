package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentEducationBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.request.addDetailResume.Qualification
import com.example.resumemaker.models.request.addDetailResume.QualificationModelRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.EducationAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationFragment : AddDetailsBaseFragment<FragmentEducationBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list=ArrayList<ProfileModelAddDetailResponse.UserQualification>()
    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner) {
            list= it.userQualifications as ArrayList<ProfileModelAddDetailResponse.UserQualification>
            setAdapter(list)
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

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(1)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(3)!!.select()
        }
        binding.addeducationbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddEducation()
        }

    }

    private fun setAdapter(userQualifications: List<ProfileModelAddDetailResponse.UserQualification>) {
        educationAdapter = EducationAdapter(currentActivity(), userQualifications, false, {
            sharePref.writeDataEdu(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddEducation()

        },{
            list.removeAt(it)
            callSaveApi()
            apiCall()
        })
        binding.recyclerviewEducation.adapter = educationAdapter
    }

    private fun callSaveApi() {
        var qualifications=ArrayList<Qualification>()
        for (i in 0 until list.size)
        {
             qualifications = listOf(
                 Qualification(
                     degree = list[i].degree,
                     endDate = list[i].endDate,
                     institute = list[i].institute,
                     qualificationType = "degree",
                     startDate = list[i].startDate
                 )
             ) as ArrayList<Qualification>
        }
        val qualificationModelRequest = QualificationModelRequest(qualifications = qualifications)

        addDetailResumeVM.editQualification(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            qualificationModelRequest
        )
    }

}