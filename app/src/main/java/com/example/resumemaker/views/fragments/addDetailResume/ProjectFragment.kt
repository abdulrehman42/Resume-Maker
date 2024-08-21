package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProjectBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.ProjectAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : AddDetailsBaseFragment<FragmentProjectBinding>() {
    val projectAdapter= ProjectAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentProjectBinding>
        get() = FragmentProjectBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setAdapter(it.userProjects)
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

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(7)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 9) {
                tabhost.getTabAt(9)!!.select()
            }else{
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()

            }
        }
        binding.addprojectbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddProjectFragment()
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun setAdapter(userProjects: List<ProfileModelAddDetailResponse.UserProject>) {
        projectAdapter.submitList(userProjects)
        projectAdapter.setOnEditItemClickCallback {
            callDeleteApi()
        }
        projectAdapter.setOnItemDeleteClickCallback {
            sharePref.writeDataProjects(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddProjectFragment()
        }
        binding.recyclerviewProjects.adapter = projectAdapter
    }

    private fun callDeleteApi() {

    }

}