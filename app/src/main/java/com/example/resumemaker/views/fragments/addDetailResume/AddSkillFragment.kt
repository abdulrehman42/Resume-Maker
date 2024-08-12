package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddSkillBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SuggestionAdapter

class AddSkillFragment : BaseFragment<FragmentAddSkillBinding>() {
    lateinit var suggestionAdapter: SuggestionAdapter
    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Skill"

        onclick()
        onAdapter()

    }

    private fun onAdapter() {
        suggestionAdapter= SuggestionAdapter(currentActivity(),Helper.getSuggestions())
        {
            binding.skillEdittext.setText(it)
        }
        binding.recyclerviewSuggestions.apply {
            layoutManager=GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter
        }


    }

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.skillEdittext.setOnFocusChangeListener { view, b ->
            if(binding.skillEdittext.textSize>=3)
            {
                binding.skillTextInputLayout.isEndIconVisible=true
            }else{
                binding.skillTextInputLayout.isEndIconVisible=false
            }
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

}