package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSkillBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class SkillFragment : AddDetailsBaseFragment<FragmentSkillBinding>() {
    lateinit var skillAdapter: SkillAdapter
    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

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
        skillAdapter= SkillAdapter(currentActivity(), Helper.getSuggestions())
        {
            sharePref.writeDataSkill(it)
            currentActivity().replaceChoiceFragment(R.id.nav_add_skill)
        }

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
            val intent= Intent(currentActivity(), ChoiceTemplate::class.java)
            intent.putExtra(Constants.FRAGMENT_NAME, Constants.SKILL)
            startActivity(intent)
           // currentActivity().replaceChoiceFragment(R.id.nav_add_skill)
        }
    }
}