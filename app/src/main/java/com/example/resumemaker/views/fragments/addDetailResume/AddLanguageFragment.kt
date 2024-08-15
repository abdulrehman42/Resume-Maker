package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddLanguageBinding
import com.example.resumemaker.models.SuggestionModel
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SuggestionAdapter


class AddLanguageFragment : BaseFragment<FragmentAddLanguageBinding>()
{
    lateinit var suggestionAdapter: SuggestionAdapter
    val list=ArrayList<SuggestionModel>()
    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Language"
        val data=sharePref.readDataSkill()
        if (data!=null)
        {
            binding.languageEdittext.setText(data.skillName)
        }
       // onAdapter()
        onclick()
    }

    private fun onAdapter() {
        suggestionAdapter= SuggestionAdapter(currentActivity(), list)
        {

        }
        binding.recyclerviewLanguage.apply {
            layoutManager= GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onclick() {
        binding.languageEdittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val enteredText = binding.languageEdittext.text.toString()
                list.add(SuggestionModel(enteredText))
                suggestionAdapter.notifyDataSetChanged()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.languageEdittext.windowToken, 0)

                true // Return true to indicate that the action has been handled
            } else {
                false
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }

    }


}
