package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentLanguageBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    lateinit var languageAdapter: SkillAdapter
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {

    }
    override fun csnMoveForward(): Boolean {
        return true
    }
    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setadapter()
    }

    private fun setadapter() {
        languageAdapter= SkillAdapter(currentActivity(), Helper.getLanuages())
        {
            sharePref.writeDataSkill(it)
            currentActivity().replaceChoiceFragment(R.id.nav_add_language)

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
            tabhost.getTabAt(8)!!.select()

        }
        binding.addlanguage.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_language)
        }
    }
}