package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddSkillBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment(
    val userSkills: List<String>,
    val position: Int?,
    val callback: SkillUpdates
) : BaseFragment<FragmentAddSkillBinding>() {
    private val screenId = ScreenIDs.ADD_SKILLS
    private val userSkillAdapter = UserSkillAdapter()
    private lateinit var skillPredictiveSearchHandler: PredictiveSearchHandler
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val list = mutableListOf<String>()
    private var alreadyUserSkills = mutableListOf<String>()
    private var isSkillInDB = false

    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_skill)
        AppsKitSDKAdsManager.showNative(requireActivity(), binding.bannerAdd, "")
        manageAds()
        populateInfoIfRequired()
        handleClicks()
        managePredictiveSearchAdapter()
        userSkillAdapter.submitList(list.toList())
        binding.recyclerviewSkill.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = userSkillAdapter
        }
    }

    private fun manageAds() {

    }

    private fun handleClicks() {
        binding.tickBtn.setOnClickListener {
            val skill = skillPredictiveSearchHandler.getText()
            if (skill.isNotEmpty()) {
                if (position!=null) {
                    list[position] = skillPredictiveSearchHandler.getText()
                } else {
                    list.add(skillPredictiveSearchHandler.getText())
                }
                binding.skillEdittext.setText("")

                userSkillAdapter.submitList(list.toList())
                binding.recyclerviewSkill.apply {
                    layoutManager = GridLayoutManager(requireContext(), 3)
                    adapter = userSkillAdapter
                }
            }
        }


        binding.savebtn.setOnClickListener {
            if (!list.isEmpty()) {
                saveSkill()
            } else {
                binding.skillTextInputLayout.error = ("please add skill")
            }

        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun saveSkill() {

        callback.skillUpdate(list)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun managePredictiveSearchAdapter() {
        skillPredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.languages,
            isAlreadyInDB = isSkillInDB,
            autoCompleteTextView = binding.skillEdittext,
            viewModel = addDetailResumeVM,
            enableBtn = binding.tickBtn
        )
    }

    private fun populateInfoIfRequired() {


        if (position != null) {
                val model = userSkills[position]
            binding.skillEdittext.setText(Helper.removeOneUnderscores(userSkills[position]))
            isSkillInDB = model.startsWith("1_")

            }
            alreadyUserSkills = userSkills as MutableList<String>
            for (i in 0 until alreadyUserSkills.size) {
                list.add(alreadyUserSkills[i])
            }
    }

    interface SkillUpdates
    {
        fun skillUpdate(skillList:List<String>)
    }
}