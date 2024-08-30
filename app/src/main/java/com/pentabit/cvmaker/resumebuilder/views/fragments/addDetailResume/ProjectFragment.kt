package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProjectBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Project
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ProjectAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
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
            AppsKitSDKUtils.setVisibility(it.userProjects.isEmpty(), binding.popup)
            list = it.userProjects as ArrayList<ProfileModelAddDetailResponse.UserProject>
            setAdapter(list)
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
            AppsKitSDKUtils.makeToast("please add at least one project")
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
        apiCall()
        onclick()
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }

    private fun onclick() {
        binding.addprojectbtn.setOnClickListener {
            addDetailResumeVM.fragment.value = AddProjectFragment(null, list, false, 0)
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setAdapter(userProjects: List<ProfileModelAddDetailResponse.UserProject>) {
        projectAdapter.submitList(userProjects)
        projectAdapter.setOnEditItemClickCallback {item,position->
            addDetailResumeVM.fragment.value = AddProjectFragment(item, list,true,position)
        }
        projectAdapter.setOnItemDeleteClickCallback {
            if (list.size!=0)
            {
                list.removeAt(it)

            }
           //setAdapter(list)
            if (list.size != 0) {
                callSaveApi()
                apiCall()

            }
        }
        binding.recyclerviewProjects.adapter = projectAdapter
    }

    private fun callSaveApi() {

        val project = ArrayList<Project>()
        for (i in 0 until list.size) {
            project.add(
                Project(list[i].description, Helper.removeOneUnderscores( list[i].title))
            )
        }
        projectRequest = ProjectRequest(projects = project)
        addDetailResumeVM.editProjects(
            projectRequest
        )
    }

}