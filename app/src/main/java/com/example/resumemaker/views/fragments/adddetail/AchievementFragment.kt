package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAchievementBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class AchievementFragment : BaseFragment<FragmentAchievementBinding>() {
    lateinit var educationAdapter: EducationAdapter

    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setAdapter()
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(8)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_preview_resume)

            // tabhost.getTabAt(9)!!.select()
        }
        binding.addachievementbtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_achievement)
        }
    }
    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.achievementList())
        binding.recyclerviewAchievements.adapter=educationAdapter
    }

}
