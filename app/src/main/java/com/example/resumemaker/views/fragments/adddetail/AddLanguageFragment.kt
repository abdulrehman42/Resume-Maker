package com.example.resumemaker.views.fragments.adddetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddLanguageBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SuggestionAdapter


class AddLanguageFragment : BaseFragment<FragmentAddLanguageBinding>()
{
    //lateinit var suggestionAdapter: SuggestionAdapter
    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Language"
       // onAdapter()
        onclick()
    }

   /* private fun onAdapter() {
        suggestionAdapter= SuggestionAdapter(currentActivity(), Helper.getSuggestions())
        {
            binding.skillEdittext.setText(it)
        }
        binding.recyclerviewLanguage.apply {
            layoutManager= GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter
        }


    }*/

    private fun onclick() {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        binding.savebtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }

    }


}
