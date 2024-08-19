package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAchievementBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>() {
    lateinit var educationAdapter: EducationAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM


    override fun csnMoveForward(): Boolean {
       return true
    }

    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]

        onclick()
        setAdapter()
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(8)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=ResumePreviewFragment()

        }
        binding.addachievementbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddAchievementsFRagment()
        }
    }
    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.achievementList(),false)
        {
            sharePref.writeDataEdu(it)
            addDetailResumeVM.isHide.value=false
            addDetailResumeVM.fragment.value=AddAchievementsFRagment()       }
        binding.recyclerviewAchievements.adapter=educationAdapter
    }

}
