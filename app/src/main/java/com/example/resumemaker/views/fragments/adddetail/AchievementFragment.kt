package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAchievementBinding

class AchievementFragment : BaseFragment<FragmentAchievementBinding>() {
    override val inflate: Inflate<FragmentAchievementBinding>
        get() = FragmentAchievementBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {

    }
}