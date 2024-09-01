package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddAchievementsFRagmentBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Achievement
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAchievementsFRagment(
    val data: ProfileModelAddDetailResponse.UserAchievement?,
    val listAchievement: ArrayList<ProfileModelAddDetailResponse.UserAchievement>?,
    val position: Int,
    val isedit: Boolean
) : BaseFragment<FragmentAddAchievementsFRagmentBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    private val screenId = ScreenIDs.ADD_ACHIEVEMENT
    val list = ArrayList<AchievementRequest>()
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserAchievement>()
    val updateList = ArrayList<Achievement>()
    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.achievementResponse.observe(this) {
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

    override fun init(savedInstanceState: Bundle?) {

        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        binding.includeTool.textView.text = getString(R.string.add_achievement)
//        val data = sharePref.readProfileAchievement()
        if (data != null) {
            binding.achieveedittext.setText(
                Helper.removeOneUnderscores(Helper.removeOneUnderscores(data.title))
            )
            binding.descriptionedittext.setText(Helper.removeOneUnderscores(data.description))
            binding.issueDateeedittext.setText(Helper.convertIsoToCustomFormat(data.issueDate))
        }
        listAchievement?.let {
            oldList = listAchievement
            for (i in 0 until oldList.size) {
                updateList.add(
                    Achievement(
                        oldList[i].description,
                        oldList[i].issueDate,
                        oldList[i].title,
                    )
                )
            }
        }
        onclick()

    }

    private fun onclick() {
        binding.savebtn.setOnClickListener {
            if (Validations.conditionAchievemnet(binding)) {
                apiCall()
            }


        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()

        }
        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
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
            DialogueBoxes.showWheelDatePickerDialog(
                currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.issueDateeedittext.setText(date)
                    }
                }
            )
        }

    }

    private fun apiCall() {
        if (isedit) {
            updateList[position] = Achievement(
                description = binding.descriptionedittext.text.toString(),
                issueDate = binding.issueDateeedittext.text.toString(),
                title = "-1__" + binding.achieveedittext.text.toString(),
            )

        } else {
            updateList.add(
                Achievement(
                    description = binding.descriptionedittext.text.toString(),
                    issueDate = binding.issueDateeedittext.text.toString(),
                    title = "-1__" + binding.achieveedittext.text.toString(),
                )
            )
        }


        val achievementRequest = AchievementRequest(achievements = updateList)

        addDetailResumeVM.editAchievement(
            achievementRequest
        )
    }


}