package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
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
class AddAchievementsFRagment(
    val listAchievement: ArrayList<ProfileModelAddDetailResponse.UserAchievement>,
    val position: Int?,
    val callback:OnAchievemntUpdate
) : BaseFragment<FragmentAddAchievementsFRagmentBinding>() {
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var titlePredictiveSearchHandler: PredictiveSearchHandler
    private var isTitleInDB = false
    private val screenId = ScreenIDs.ADD_ACHIEVEMENT
    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_achievement)

        manageAds()
        populateInfoIfRequired()
        handleClicks()
        managePredictiveSearchAdapter()
    }

    private fun managePredictiveSearchAdapter() {
        titlePredictiveSearchHandler =
            PredictiveSearchHandler(
                key = Constants.degree,
                isAlreadyInDB = isTitleInDB,
                autoCompleteTextView = binding.achieveedittext,
                viewModel = addDetailResumeVM
            )
    }

    private fun handleClicks() {

        binding.issueDateeedittext.setOnClickListener {
            DialogueBoxes.showWheelDatePickerDialog(
                currentActivity(),
                object : DialogueBoxes.StringDialogCallback {
                    override fun onButtonClick(date: String) {
                        binding.issueDateeedittext.setText(date)
                    }
                })
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()

        }
        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

        binding.savebtn.setOnClickListener {
            if (Validations.conditionAchievemnet(binding)) {
                saveAchievement()
            }
        }
    }

    private fun saveAchievement() {
        val newAchievement = ProfileModelAddDetailResponse.UserAchievement(title = titlePredictiveSearchHandler.getText(),
            description =binding.descriptionedittext.text.toString() , issueDate = Helper.convertToUTCTimeForma(binding.issueDateeedittext.text.toString())
        )

        val updatedList = ArrayList<ProfileModelAddDetailResponse.UserAchievement>(listAchievement)

        if (position != null) {
            updatedList[position] = newAchievement
        } else {
            updatedList.add(newAchievement)
        }
        callback.onAchievemnet(updatedList)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun populateInfoIfRequired() {
        if (position != null) {
            val model = listAchievement[position]
            isTitleInDB = model.title.startsWith("1_")

            binding.achieveedittext.setText(Helper.removeOneUnderscores(model.title))
            binding.descriptionedittext.setText(model.description)
            binding.issueDateeedittext.setText(
                Helper.convertIsoToCustomFormat(model.issueDate)
            )

        }
    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }



    interface OnAchievemntUpdate{
        fun onAchievemnet(listAchievement: ArrayList<ProfileModelAddDetailResponse.UserAchievement>)
    }


}