package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentEducationBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class EducationFragment : AddDetailsBaseFragment<FragmentEducationBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun observeLiveData() {
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
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
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddEducation()
        }

    }

    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(),Helper.getDegreeList(),false){
            sharePref.writeDataEdu(it)
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddEducation()

        }
        binding.recyclerviewEducation.adapter=educationAdapter
    }

}