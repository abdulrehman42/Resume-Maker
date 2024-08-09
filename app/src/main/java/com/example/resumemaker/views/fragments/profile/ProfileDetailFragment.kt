package com.example.resumemaker.views.fragments.profile

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProfileBinding
import com.example.resumemaker.databinding.FragmentProfileDetailBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.views.adapter.EducationAdapter
import com.example.resumemaker.views.adapter.ExperienceProfAdapter
import com.example.resumemaker.views.adapter.SkillProfAdapter

class ProfileDetailFragment : BaseFragment<FragmentProfileDetailBinding>()
{
    override val inflate: Inflate<FragmentProfileDetailBinding>
        get() = FragmentProfileDetailBinding::inflate

    override fun observeLiveData() {
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text="User Profile"
        setValues()

    }

    private fun setValues() {
        val data=sharePref.readDataProfile()
        val skillAdapter=SkillProfAdapter(currentActivity(),data.detail.skill)
         val experienceAdapter=ExperienceProfAdapter(currentActivity(),data.detail.exp)
        val eduAdapter=EducationAdapter(currentActivity(),data.detail.edu,true)
        val certificate=EducationAdapter(currentActivity(),data.detail.certi,true)

        binding.apply {
            userName.text=data.userName
            userIntro.text=data.detail.intro
            gender.text=data.detail.gender
            location.text=data.detail.location
            objexttext.text=data.detail.obj
            skillRecyclerview.apply {
                layoutManager=GridLayoutManager(currentActivity(),3)
            }
            experienceRecyclerview.adapter=experienceAdapter
            skillRecyclerview.adapter=skillAdapter
            educationRecyclerview.adapter=eduAdapter
            certificateRecyclerview.adapter=certificate
        }
           }

}