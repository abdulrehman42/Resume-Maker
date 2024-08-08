package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentEducationBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class EducationFragment : BaseFragment<FragmentEducationBinding>() {
    lateinit var educationAdapter: EducationAdapter
    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setAdapter()
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(1)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(3)!!.select()
        }
        binding.addeducationbtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_education)
        }

    }

    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(),Helper.getDegreeList())
        binding.recyclerviewEducation.adapter=educationAdapter
    }

}