package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProjectBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ProjectAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : AddDetailsBaseFragment<FragmentProjectBinding>(),AddProjectFragment.OnAddProjectUpdate {
    val projectAdapter = ProjectAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var isCalled=false
    var list = ArrayList<ProfileModelAddDetailResponse.UserProject>()
    override val inflate: Inflate<FragmentProjectBinding>
        get() = FragmentProjectBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
        fetchProfileData()
    }


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userProjects.isNullOrEmpty(), binding.popup)
            populateAdapter(it.userProjects as ArrayList<ProfileModelAddDetailResponse.UserProject>)
            isCalled=true
        }
    }

    private fun handleAdapter() {
        projectAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){
                deleteItemPopup(currentActivity(), "Do you want to delete this project record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    projectAdapter.submitList(list)
                                    projectAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            }else{
                AppsKitSDKUtils.makeToast("sorry at least one project required")

            }
        }

        projectAdapter.setOnEditItemClickCallback { _, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddProjectFragment(
                    list,
                    position,
                    this
                )
            )
        }
        binding.recyclerviewProjects.adapter = projectAdapter

    }


    private fun handleClicks() {
        binding.addprojectbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(AddProjectFragment(list, null, this))
        }
    }

    private fun populateAdapter(qualificationList: List<ProfileModelAddDetailResponse.UserProject>) {
        if (!isCalled)
        {
            saveInListWithRequiredFormat(qualificationList)
            projectAdapter.submitList(list)
        }
    }

    private fun saveInListWithRequiredFormat(projectList: List<ProfileModelAddDetailResponse.UserProject>) {
        for (projects in projectList) {
            val model = ProfileModelAddDetailResponse.UserProject(
                description = projects.description,
                title = "1__" + Helper.removeOneUnderscores(projects.title),
            )
            list.add(model)
        }
    }

    private fun saveProjectsData() {
        addDetailResumeVM.editProjects(
            list
        )
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty())
        {
            AppsKitSDKUtils.makeToast("please Add at least one project")
            return false

        }else{
            saveProjectsData()
            return false
        }
    }

    private fun fetchProfileData() {
        if (!isCalled)
        {
            addDetailResumeVM.getProfileDetail()
        }
    }

    override fun onProject(projectList: List<ProfileModelAddDetailResponse.UserProject>) {
        list = ArrayList(projectList)
        AppsKitSDKUtils.setVisibility(projectList.isEmpty(), binding.popup)
        projectAdapter.submitList(projectList)
    }

}