package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.utils.Helper.dpToPx
import com.google.android.material.textfield.TextInputLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddProjectBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Project
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddProjectFragment(
    val data: ProfileModelAddDetailResponse.UserProject?,
    val userProjectsList: List<ProfileModelAddDetailResponse.UserProject>?,
    val isedit: Boolean,
    val position: Int
) :
    BaseFragment<FragmentAddProjectBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserProject>()
    val updateList = ArrayList<Project>()
    override val inflate: Inflate<FragmentAddProjectBinding>
        get() = FragmentAddProjectBinding::inflate

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

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_project)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        userProjectsList?.let {
            oldList = userProjectsList as ArrayList<ProfileModelAddDetailResponse.UserProject>
            for (i in 0 until oldList.size) {
                updateList.add(
                    Project(
                        oldList[i].description,
                        Helper.removeOneUnderscores(oldList[i].title),
                    )
                )
            }
        }
        data?.let {
            binding.projectedittext.setText(Helper.removeOneUnderscores(data.title))
            binding.descriptionedittext.setText(Helper.removeOneUnderscores(data.description))
        }
        binding.projecttitleTextInputLayout.apply {
            // Adjust the layout parameters of the end icon
            endIconMode = TextInputLayout.END_ICON_CUSTOM // If not already set
            val endIconView =
                findViewById<ImageView>(com.google.android.material.R.id.text_input_end_icon)

            val params = endIconView.layoutParams as FrameLayout.LayoutParams
            params.gravity = Gravity.BOTTOM or Gravity.END
            params.marginEnd = 8.dpToPx(context) // Adjust margin to your needs
            params.bottomMargin = 8.dpToPx(context)
            endIconView.layoutParams = params
        }
        onclick()

    }


    private fun onclick() {
        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetProject(binding)) {
                apiCall()
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()

        }
        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

    }


    private fun apiCall() {
        if (!isedit)
        {
            updateList.add(
                Project(
                    binding.descriptionedittext.text.toString(),
                    binding.projectedittext.text.toString()
                )
            )
        }else{
            updateList[position]= Project(
                binding.descriptionedittext.text.toString(),
                binding.projectedittext.text.toString()
            )
        }


        val projectRequest = ProjectRequest(projects = updateList)

        addDetailResumeVM.editProjects(
            projectRequest
        )
    }
}