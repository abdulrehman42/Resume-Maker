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
            addDetailResumeVM.fragment.value = AddProjectFragment()
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString())
    }

    private fun setAdapter(userProjects: List<ProfileModelAddDetailResponse.UserProject>) {
        projectAdapter.submitList(userProjects)
        projectAdapter.setOnEditItemClickCallback {
            sharePref.writeDataProjects(it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddProjectFragment()
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
            sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString(), projectRequest
        )
    }

}