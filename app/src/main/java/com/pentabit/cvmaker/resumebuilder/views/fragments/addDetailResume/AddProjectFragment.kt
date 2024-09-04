package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnEducationUpdate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddProjectBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Project
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.Helper.dpToPx
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectFragment(
    val userProjectsList: List<ProfileModelAddDetailResponse.UserProject>,
    val position: Int?,
    val callback: OnAddProjectUpdate
) :
    BaseFragment<FragmentAddProjectBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    val screenId = ScreenIDs.ADD_PROJECT
    override val inflate: Inflate<FragmentAddProjectBinding>
        get() = FragmentAddProjectBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_project)
        manageAds()
        populateInfoIfRequired()
        handleClicks()

    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }


    private fun populateInfoIfRequired() {
        if (position != null) {
            val model=userProjectsList[position]
            binding.projectedittext.setText(Helper.removeOneUnderscores(model.title))
            binding.descriptionedittext.setText(model.description)
        }
    }


    private fun handleClicks() {
        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetProject(binding)) {
                saveProject()
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun saveProject() {
        val newProject = ProfileModelAddDetailResponse.UserProject(
            title = binding.projectedittext.text.toString(),
            description = binding.descriptionedittext.text.toString(),
        )

        val updatedList =
            ArrayList<ProfileModelAddDetailResponse.UserProject>(userProjectsList)

        if (position != null) {
            updatedList[position] = newProject
        } else {
            updatedList.add(newProject)
        }
        callback.onProject(updatedList)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }


    override fun observeLiveData() {
        addDetailResumeVM.projectResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }


    interface  OnAddProjectUpdate{
        fun onProject(projectList:List<ProfileModelAddDetailResponse.UserProject>)
    }
}