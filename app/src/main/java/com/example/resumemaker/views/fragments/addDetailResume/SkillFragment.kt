package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSkillBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.adddetailresume.LanguageAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillFragment : AddDetailsBaseFragment<FragmentSkillBinding>() {
    lateinit var skillAdapter: LanguageAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM

    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setadapter(it.userSkills)
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun setadapter(userSkills: List<String>) {
        skillAdapter.submitList(userSkills)
        skillAdapter.setOnEditItemClickCallback {
            callDeleteApi()
        }
        skillAdapter.setOnItemDeleteClickCallback {
            sharePref.writeString(Constants.DATA,it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddSkillFragment()
        }

        binding.recyclerviewSkill.apply {
            adapter = skillAdapter
        }
    }

    private fun callDeleteApi() {

    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(2)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(4)!!.select()

        }
        binding.addskill.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddSkillFragment()
        }
    }
}