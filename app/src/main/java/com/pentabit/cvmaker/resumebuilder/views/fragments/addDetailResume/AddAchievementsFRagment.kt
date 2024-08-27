package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddAchievementsFRagmentBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Achievement
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAchievementsFRagment(val data: ProfileModelAddDetailResponse.UserAchievement?) : BaseFragment<FragmentAddAchievementsFRagmentBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    val list = ArrayList<AchievementRequest>()

    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.achievementResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
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

    override fun init(savedInstanceState: Bundle?) {

        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        binding.includeTool.textView.text = getString(R.string.add_achievement)
//        val data = sharePref.readProfileAchievement()
        if (data != null) {
            binding.achieveedittext.setText(data.title)
            binding.descriptionedittext.setText(data.description)
            binding.issueDateeedittext.setText(data.issueDate)
        }
        onclick()

    }

    private fun onclick() {
        binding.savebtn.setOnClickListener {

            apiCall()

        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            addDetailResumeVM.isHide.value = true
//        }
        binding.issueDateeedittext.onFocusChangeListener =
            OnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object :
                            com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.StringDialogCallback {
                            override fun onButtonClick(date: String) {
                                binding.issueDateeedittext.setText(date)
                            }
                        })
                }
            }
        binding.issueDateeedittext.setOnClickListener {

        }
    }

    private fun apiCall() {
        val achievemnt = listOf(
            Achievement(
                description = "-1__" + binding.descriptionedittext.text.toString(),
                issueDate = binding.issueDateeedittext.text.toString(),
                title = "-1__" + binding.achieveedittext.text.toString(),
            )
        )

        val achievementRequest = AchievementRequest(achievements = achievemnt)

        addDetailResumeVM.editAchievement(
             achievementRequest
        )
    }


}