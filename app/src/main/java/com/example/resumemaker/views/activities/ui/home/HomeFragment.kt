package com.example.resumemaker.views.activities.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
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
            requireActivity().startActivity(Intent(requireActivity(),ProfileActivity::class.java))

        }

    }


}