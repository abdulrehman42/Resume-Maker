package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddSkillBinding
import com.example.resumemaker.models.SuggestionModel
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SuggestionAdapter
import com.google.android.material.textfield.TextInputLayout

class AddSkillFragment : BaseFragment<FragmentAddSkillBinding>() {
    lateinit var suggestionAdapter: SuggestionAdapter
    lateinit var suggestionAdapter1: SuggestionAdapter

    val list=ArrayList<SuggestionModel>()
    override val inflate: Inflate<FragmentAddSkillBinding>
        get() = FragmentAddSkillBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Skill"
        val data=sharePref.readDataSkill()
        if (data!=null)
        {
            binding.skillEdittext.setText(data.skillName)
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
        suggestionAdapter= SuggestionAdapter(currentActivity(),Helper.getSuggestions())
        {
            binding.skillEdittext.setText(it)
        }
        suggestionAdapter1= SuggestionAdapter(currentActivity(),list)
        {
            binding.skillEdittext.setText(it)
        }

        binding.recyclerviewSuggestions.apply {
            layoutManager=GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter
        }
        binding.recyclerviewSkill.apply {
            layoutManager=GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter1
        }


    }

    private fun onclick() {
        binding.skillTextInputLayout.setEndIconOnClickListener {
            binding.skillEdittext.setText("")
            list.add(SuggestionModel(binding.skillEdittext.text.toString()))
            suggestionAdapter1= SuggestionAdapter(currentActivity(),list)
            {
                binding.skillEdittext.setText(it)
            }
            binding.recyclerviewSkill.apply {
                layoutManager=GridLayoutManager(currentActivity(),3)
                adapter=suggestionAdapter1
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()

        //            currentActivity().onBackPressedDispatcher.onBackPressed()
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
            currentActivity().finish()

//            currentActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

}