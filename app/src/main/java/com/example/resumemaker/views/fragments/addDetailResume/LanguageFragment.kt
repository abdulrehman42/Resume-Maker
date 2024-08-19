package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentLanguageBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    lateinit var languageAdapter: SkillAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

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
        languageAdapter= SkillAdapter(currentActivity(), Helper.getLanuages())
        {
            sharePref.writeDataSkill(it)
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddLanguageFragment()
        }

        binding.recyclerviewLanguage.apply {
            adapter=languageAdapter
        }
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(6)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount>=8)
            {
                tabhost.getTabAt(8)!!.select()
            }

        }
        binding.addlanguage.setOnClickListener {
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddEducation()
        }
    }
}