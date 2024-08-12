package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProjectBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout


class ProjectFragment : BaseFragment<FragmentProjectBinding>(){
    lateinit var educationAdapter: EducationAdapter
    override val inflate: Inflate<FragmentProjectBinding>
        get() = FragmentProjectBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setAdapter()
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(7)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(9)!!.select()
        }
        binding.addprojectbtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_projects)
        }
    }
    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.projectsList(),false)
        binding.recyclerviewProjects.adapter=educationAdapter
    }

    }