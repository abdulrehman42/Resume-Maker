package com.example.resumemaker.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAchievementBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.EducationAdapter
import com.google.android.material.tabs.TabLayout

class AchievementFragment : AddDetailsBaseFragment<FragmentAchievementBinding>() {
    lateinit var educationAdapter: EducationAdapter

    override fun csnMoveForward(): Boolean {
       return true
    }

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
            val intent=Intent(currentActivity(),ChoiceTemplate::class.java)
            intent.putExtra(Constants.FRAGMENT_NAME,Constants.PREVIEW_RESUME)
            startActivity(intent)

        }
        binding.addachievementbtn.setOnClickListener {
            val intent=Intent(currentActivity(),ChoiceTemplate::class.java)
            intent.putExtra(Constants.FRAGMENT_NAME,Constants.ACHIEVEMNT)
            startActivity(intent)
        }
    }
    private fun setAdapter() {
        educationAdapter= EducationAdapter(currentActivity(), Helper.achievementList(),false)
        {
            sharePref.writeDataEdu(it)
            val intent=Intent(currentActivity(),ChoiceTemplate::class.java)
            intent.putExtra(Constants.FRAGMENT_NAME,Constants.ACHIEVEMNT)
            startActivity(intent)        }
        binding.recyclerviewAchievements.adapter=educationAdapter
    }

}
