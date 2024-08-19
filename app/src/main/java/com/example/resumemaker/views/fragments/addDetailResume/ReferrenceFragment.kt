package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentReferrenceBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout


class ReferrenceFragment : AddDetailsBaseFragment<FragmentReferrenceBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentReferrenceBinding>
        get() = FragmentReferrenceBinding::inflate

    override fun observeLiveData() {

    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]

        onclick()
        setadapter()
    }

    private fun setadapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.getreferrenceList(),false)
        {
            sharePref.writeDataEdu(it)
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddReferenceFragment()       }
        binding.recyclerviewReference.adapter=educationAdapter
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(4)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount>=7)
            {
                tabhost.getTabAt(6)!!.select()
            }
        }
        binding.addreferrenebtn.setOnClickListener {
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddReferenceFragment()
        }
    }
}