package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddSkillBinding
import com.example.resumemaker.models.SuggestionModel
import com.example.resumemaker.models.request.addDetailResume.SkillRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.SuggestionAdapter
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment : BaseFragment<FragmentAddSkillBinding>() {
    lateinit var suggestionAdapter1: SuggestionAdapter
    val data=sharePref.readProfileData()
    val addDetailResumeVM by viewModels<AddDetailResumeVM>()
    var list = ArrayList<String>()


    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_skill)
        list= data?.userSkills as ArrayList<String>
        val data = sharePref.readString(Constants.DATA)
        if (data != null) {
            binding.skillEdittext.setText(data)
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

            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed here
            }
        })
        onclick()
        onAdapter()

    }

    private fun onAdapter() {
        /*suggestionAdapter = SuggestionAdapter(currentActivity(), Helper.getSuggestions())
        {
            binding.skillEdittext.setText(it)
        }*/
        suggestionAdapter1 = SuggestionAdapter(currentActivity(), list)
        {
            binding.skillEdittext.setText(it)
        }

        /*binding.recyclerviewSuggestions.apply {
            layoutManager = GridLayoutManager(currentActivity(), 3)
            adapter = suggestionAdapter
        }*/
        binding.recyclerviewSkill.apply {
            layoutManager = GridLayoutManager(currentActivity(), 3)
            adapter = suggestionAdapter1
        }


    }

    private fun onclick() {
        binding.skillTextInputLayout.setEndIconOnClickListener {
            binding.skillEdittext.setText("")
            list.add(binding.skillEdittext.text.toString())
            suggestionAdapter1 = SuggestionAdapter(currentActivity(), list)
            {
                binding.skillEdittext.setText(it)
            }
            binding.recyclerviewSkill.apply {
                layoutManager = GridLayoutManager(currentActivity(), 3)
                adapter = suggestionAdapter1
            }
        }
        binding.savebtn.setOnClickListener {
            if (binding.skillEdittext.text.toString().length > 3) {
                apiCall()
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
            sharePref.readString(Constants.PROFILE_ID).toString(),
            SkillRequestModel(list)
        )
    }

}