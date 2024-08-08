package com.example.resumemaker.views.fragments.adddetail

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInformationBinding
import com.google.android.material.tabs.TabLayout


class InformationFragment : BaseFragment<FragmentInformationBinding>(){
    override val inflate: Inflate<FragmentInformationBinding>
        get() = FragmentInformationBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
    }

    @SuppressLint("ResourceAsColor")
    private fun onclick() {
        binding.man.setOnClickListener {

            binding.man.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.dark_navy_blue))
            binding.woman.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_black))
            binding.man.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.woman.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_dim))

        }
        binding.woman.setOnClickListener {
            binding.man.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.grey_dim))
            binding.woman.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            binding.man.setTextColor(ContextCompat.getColor(requireContext(),R.color.light_black))
            binding.woman.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.dark_navy_blue))
        }
        binding.nextbtn.setOnClickListener {
            val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
            tabhost.getTabAt(1)!!.select()        }
    }

}