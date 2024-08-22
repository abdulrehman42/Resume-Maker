package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddAchievementsFRagmentBinding
import com.example.resumemaker.models.request.addDetailResume.Achievement
import com.example.resumemaker.models.request.addDetailResume.AchievementRequest
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddAchievementsFRagment : BaseFragment<FragmentAddAchievementsFRagmentBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    val list = ArrayList<AchievementRequest>()

    override val inflate: Inflate<FragmentAddAchievementsFRagmentBinding>
        get() = FragmentAddAchievementsFRagmentBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.achievementResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {

        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]

        binding.includeTool.textView.text = getString(R.string.add_achievement)
        val data = sharePref.readProfileAchievement()
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
                    DialogueBoxes.showWheelDatePickerDialog(
                        currentActivity(),
                        object : DialogueBoxes.StringDialogCallback {
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
                description = "1__" + binding.descriptionedittext.text.toString(),
                issueDate = binding.issueDateeedittext.text.toString(),
                title = "1__" + binding.achieveedittext.text.toString(),
            )
        )

        val achievementRequest = AchievementRequest(achievements = achievemnt)

        addDetailResumeVM.editAchievement(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            achievementRequest
        )
    }


}