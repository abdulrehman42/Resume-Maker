package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProjectBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Project
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ProjectAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : AddDetailsBaseFragment<FragmentProjectBinding>() {
    val projectAdapter = ProjectAdapter()
    lateinit var projectRequest: ProjectRequest
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<ProfileModelAddDetailResponse.UserProject>()
    override val inflate: Inflate<FragmentProjectBinding>
        get() = FragmentProjectBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list = it.userProjects as ArrayList<ProfileModelAddDetailResponse.UserProject>
            setAdapter(list)
        }

        addDetailResumeVM.projectResponse.observe(viewLifecycleOwner) {
            apiCall()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            if (it.loader) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
            if (!it.msg.isNullOrBlank()) {
                currentActivity().showToast(it.msg)
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
            } else {
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()

            }
        }
        binding.addprojectbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddProjectFragment(null)
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(
        )
    }

    private fun setAdapter(userProjects: List<ProfileModelAddDetailResponse.UserProject>) {
        projectAdapter.submitList(userProjects)
        projectAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddProjectFragment(it)
        }
        projectAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            setAdapter(list)
            callSaveApi()
            apiCall()
        }
        binding.recyclerviewProjects.adapter = projectAdapter
    }

    private fun callSaveApi() {

        var project = ArrayList<Project>()
        for (i in 0 until list.size) {
            project = listOf(
                Project(list[i].description, list[i].title)
            ) as ArrayList<Project>
        }
        projectRequest = ProjectRequest(projects = project)
        addDetailResumeVM.editProjects(
            projectRequest
        )
    }

}