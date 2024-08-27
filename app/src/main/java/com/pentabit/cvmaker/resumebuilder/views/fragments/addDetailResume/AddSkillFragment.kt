package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddSkillBinding
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.SuggestionAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment(val userSkills: List<String>?, val skill_name: String?) : BaseFragment<FragmentAddSkillBinding>() {
    lateinit var suggestionAdapter1: SuggestionAdapter
    val userSkillAdapter= UserSkillAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<String>()
    var alreadyUserSkills=ArrayList<String>()


    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.skillResponse.observe(currentActivity()) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        addDetailResumeVM.looksupResponse.observe(currentActivity()) {
            onAdapter(it)
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            if (it.loader)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
            if (!it.msg.isNullOrBlank())
            {
                currentActivity().showToast(it.msg)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_skill)
        AppsKitSDKAdsManager.showNative(currentActivity(),binding.bannerAdd,""
        )
        userSkills?.let {
            alreadyUserSkills= userSkills as ArrayList<String>
        }
        skill_name?.let {
            binding.skillEdittext.setText(skill_name)
        }
        binding.skillEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the text length is greater than 3
                if (binding.skillEdittext.text!!.length > 3) {
                    binding.skillTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM)
                    binding.skillTextInputLayout.setEndIconDrawable(R.drawable.tick_green)
                } else {
                    binding.skillTextInputLayout.setEndIconMode(TextInputLayout.END_ICON_NONE)
                }

                val query = binding.skillEdittext.text.toString()
                callLookUpApi(query)

            }

            override fun afterTextChanged(s: Editable?) {
                val query = binding.skillEdittext.text.toString()
                callLookUpApi(query)
            }
        })
        onclick()

    }

    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.skills, query, "", "")

    }

    private fun onAdapter(lookUpResponses: List<LookUpResponse>) {
        suggestionAdapter1 = SuggestionAdapter(lookUpResponses)
        {
            binding.skillEdittext.setText(it.text)
        }
        binding.recyclerviewSuggestions.apply {
            layoutManager = GridLayoutManager(currentActivity(), 3)
            adapter = suggestionAdapter1
        }
        binding.recyclerviewSkill.apply {
            layoutManager = GridLayoutManager(currentActivity(), 3)
            adapter = userSkillAdapter
        }


    }

    private fun onclick() {
        binding.skillTextInputLayout.isEnabled = true
        binding.skillTextInputLayout.setEndIconOnClickListener {
            val skill = binding.skillEdittext.text.toString().trim()
            if (skill.isNotEmpty()) {
                list.add(skill) // Add the captured text to the list
                userSkillAdapter.submitList(list.toList()) // Update the adapter with a new list
                binding.skillEdittext.setText("") // Clear the text after adding it to the list
            }
        }

        binding.savebtn.setOnClickListener {
            if (binding.skillEdittext.text.toString().length > 3) {
                list.add("1__" + binding.skillEdittext.text.toString())
                apiCall()
                /*MainScope().launch {
                    delay(2000)
                    addDetailResumeVM.isHide.value = true
                    currentActivity().onBackPressedDispatcher.onBackPressed()
                }*/
            } else {
                currentActivity().showToast(getString(R.string.single_field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        currentActivity().onBackPressedDispatcher.addCallback(currentActivity()) {
            addDetailResumeVM.isHide.value = true
        }
        binding.skillEdittext.setOnFocusChangeListener { view, b ->
            if (binding.skillEdittext.textSize >= 3) {
                binding.skillTextInputLayout.isEndIconVisible = true
            } else {
                binding.skillTextInputLayout.isEndIconVisible = false
            }
        }

    }

    private fun apiCall() {
        addDetailResumeVM.editSkill(
            SkillRequestModel(list)
        )
    }

}