package com.example.resumemaker.views.fragments

import android.content.Intent
import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentHomeBinding
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.activities.ProfileActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val inflate: Inflate<FragmentHomeBinding>
        get() = FragmentHomeBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
    }

    private fun onclick() {
        binding.linearLayout1.setOnClickListener {

            requireActivity().startActivity(Intent(requireActivity(),ChoiceTemplate::class.java))
        }
        binding.linearLayout2.setOnClickListener {
            requireActivity().startActivity(Intent(requireActivity(),ChoiceTemplate::class.java))

        }
        binding.linearLayout4.setOnClickListener {
            requireActivity().startActivity(Intent(currentActivity(),ProfileActivity::class.java))
        }

    }


}