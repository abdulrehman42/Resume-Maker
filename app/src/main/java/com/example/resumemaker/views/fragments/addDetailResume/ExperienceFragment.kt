package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentExperienceBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.ExperienceAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment : AddDetailsBaseFragment<FragmentExperienceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    val experienceAdapter=ExperienceAdapter()
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setadapter(it.userExperiences)
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

    private fun setadapter(userExperiences: List<ProfileModelAddDetailResponse.UserExperience>) {
        experienceAdapter.submitList(userExperiences)
        experienceAdapter.setOnEditItemClickCallback {
            callDeleteApi()
        }
        experienceAdapter.setOnItemDeleteClickCallback {
            sharePref.writeDataExperience(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddExperienceFragment()
        }
        binding.recyclerviewExperience.adapter = experienceAdapter
    }

    private fun callDeleteApi() {

    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(3)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()
        }
        binding.addexperiencebtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddExperienceFragment()

        }
    }


}