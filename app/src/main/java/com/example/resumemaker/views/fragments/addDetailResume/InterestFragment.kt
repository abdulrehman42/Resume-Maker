package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInterestBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.SkillAdapter
import com.google.android.material.tabs.TabLayout

class InterestFragment : BaseFragment<FragmentInterestBinding>() {
    lateinit var educationAdapter: SkillAdapter

    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
     }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        setadapter()
    }

    private fun setadapter() {
        educationAdapter= SkillAdapter(currentActivity(), Helper.getInterests())
        {
            sharePref.writeDataSkill(it)
            currentActivity().replaceChoiceFragment(R.id.nav_add_language)

        }
        binding.recyclerviewInterest.adapter=educationAdapter
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(5)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(7)!!.select()
        }
        binding.addinterestbtn.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_Add_interest)
        }
    }


}