package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddSkillBinding
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.SuggestionAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment(
    val userSkills: List<String>?,
    val skillName: String?,
    val position: Int,
    val isedit: Boolean
) : BaseFragment<FragmentAddSkillBinding>() {
    private lateinit var suggestionAdapter: SuggestionAdapter
    private val userSkillAdapter = UserSkillAdapter()
    private lateinit var addDetailResumeVM: AddDetailResumeVM
    private val list = mutableListOf<String>()
    var startWord = "-1__"
    private var alreadyUserSkills = mutableListOf<String>()

    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.skillResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.looksupResponse.observe(this) {
            setupAdapters(it)
        }
        addDetailResumeVM.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_skill)
        AppsKitSDKAdsManager.showNative(requireActivity(), binding.bannerAdd, "")

        userSkills?.let {
            alreadyUserSkills = userSkills as MutableList<String>
            for (i in 0 until alreadyUserSkills.size) {
                list.add(alreadyUserSkills[i])
            }
        }
        skillName?.let {
            binding.skillEdittext.setText(Helper.removeOneUnderscores(it))
        }


        binding.skillEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                if (textLength > 2) {
                    binding.tickBtn.isGone = false
                } else {
                    binding.tickBtn.isGone = true
                }
                val query = s.toString()
                callLookUpApi(query)
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                callLookUpApi(query)
            }

        })
        userSkillAdapter.submitList(userSkills)

        binding.recyclerviewSkill.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = userSkillAdapter
        }
        setupClickListeners()
    }


    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.skills, query, "", "6")
    }

    private fun setupAdapters(lookUpResponses: List<LookUpResponse>) {
        suggestionAdapter = SuggestionAdapter(lookUpResponses) {
            binding.skillEdittext.setText(it.text)
        }
        binding.recyclerviewSuggestions.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = suggestionAdapter
        }
        binding.recyclerviewSkill.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = userSkillAdapter
        }
    }

    private fun setupClickListeners() {
        binding.tickBtn.setOnClickListener {
            val skill = binding.skillEdittext.text.toString().trim()
            if (skill.isNotEmpty()) {
                if (isedit)
                {
                    list[position]=startWord+skill
                }else{
                    list.add(startWord + skill)
                }
                binding.skillEdittext.setText("")

                userSkillAdapter.submitList(list.toList())
            }
        }


        binding.savebtn.setOnClickListener {
            if (!list.isEmpty())
            {
                apiCall()
            }else{
                AppsKitSDKUtils.makeToast("please add skill")
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

        addDetailResumeVM.editSkill(SkillRequestModel(list))
    }
}