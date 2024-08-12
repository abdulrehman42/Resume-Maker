package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSkillBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class SkillFragment : BaseFragment<FragmentSkillBinding>() {
    lateinit var skillAdapter: SkillAdapter
    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setadapter()
    }

    private fun setadapter() {
        skillAdapter= SkillAdapter(currentActivity(), Helper.getSuggestions())

        binding.recyclerviewSkill.apply {
            adapter=skillAdapter
        }
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
            currentActivity().replaceChoiceFragment(R.id.nav_add_skill)
        }
    }
}