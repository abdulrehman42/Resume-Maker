package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentExperienceBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class ExperienceFragment : BaseFragment<FragmentExperienceBinding>() {
    lateinit var educationAdapter: EducationAdapter
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setadapter()
    }

    private fun setadapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.getExperienceList(),false)
        {
            sharePref.writeDataEdu(it)
            currentActivity().replaceChoiceFragment(R.id.nav_add_experience)

        }
        binding.recyclerviewExperience.adapter=educationAdapter
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(3)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()
        }
        binding.addexperiencebtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_experience)
        }
    }


}