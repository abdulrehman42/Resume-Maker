package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentExperienceBinding

class ExperienceFragment : BaseFragment<FragmentExperienceBinding>() {
    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
    }


}