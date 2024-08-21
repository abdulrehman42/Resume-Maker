package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentEducationBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.EducationAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationFragment : AddDetailsBaseFragment<FragmentEducationBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setAdapter(it.userQualifications)
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
        educationAdapter = EducationAdapter(currentActivity(), userQualifications, false) {
            sharePref.writeDataEdu(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddEducation()

        }
        binding.recyclerviewEducation.adapter = educationAdapter
    }

}