package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInterestBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class InterestFragment : AddDetailsBaseFragment<FragmentInterestBinding>() {
    lateinit var educationAdapter: SkillAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

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
        educationAdapter= SkillAdapter(currentActivity(), Helper.getInterests())
        {
            sharePref.writeDataSkill(it)
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddInterestFragment()
        }
        binding.recyclerviewInterest.adapter=educationAdapter
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount>=7)
            {
                tabhost.getTabAt(7)!!.select()
            }
        }
        binding.addinterestbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddInterestFragment()
        }
    }


}