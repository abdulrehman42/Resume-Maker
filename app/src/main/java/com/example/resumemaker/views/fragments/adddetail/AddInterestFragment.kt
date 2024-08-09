package com.example.resumemaker.views.fragments.adddetail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddInterestBinding
import com.example.resumemaker.databinding.FragmentAddSkillBinding
import com.example.resumemaker.databinding.FragmentInterestBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SkillAdapter
import com.example.resumemaker.views.adapter.SuggestionAdapter

class AddInterestFragment : BaseFragment<FragmentAddInterestBinding>()
{
   // lateinit var addinterest: SuggestionAdapter
    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="Add Interest"

        onclick()
        //onAdapter()

    }

   /* private fun onAdapter() {
        addinterest= SuggestionAdapter(currentActivity(), Helper.getSuggestions())
        {
            binding.interestEdittext.setText(it)
        }

        binding.recyclerviewSuggestions.apply {
            layoutManager= GridLayoutManager(currentActivity(),3)
            adapter=addinterest
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