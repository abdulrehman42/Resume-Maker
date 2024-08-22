package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentExperienceBinding
import com.example.resumemaker.models.api.ProfileListingModel
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.adddetailresume.ExperienceResponse
import com.example.resumemaker.models.request.addDetailResume.Experience
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.ExperienceAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment : AddDetailsBaseFragment<FragmentExperienceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list=ArrayList<ProfileModelAddDetailResponse.UserExperience>()
    val experienceAdapter=ExperienceAdapter()
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list= it.userExperiences as ArrayList<ProfileModelAddDetailResponse.UserExperience>
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

    private fun setadapter(userExperiences: List<ProfileModelAddDetailResponse.UserExperience>) {
        experienceAdapter.submitList(userExperiences)
        experienceAdapter.setOnEditItemClickCallback {
            sharePref.writeDataExperience(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddExperienceFragment()

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            callSaveApi()
            apiCall()

               }
        binding.recyclerviewExperience.adapter = experienceAdapter
    }

    private fun callSaveApi() {
        var experience=ArrayList<Experience>()
        for (i in 0 until list.size)
        {
            experience = listOf(
                Experience(
                    list[i].company,
                    list[i].description,
                    "fullTime",
                    list[i].endDate,list[i].startDate,list[i].title
                )
            ) as ArrayList<Experience>

        }
        val experienceRequest = ExperienceRequest(experiences = experience)
        addDetailResumeVM.editExperience(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            experienceRequest
        )
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