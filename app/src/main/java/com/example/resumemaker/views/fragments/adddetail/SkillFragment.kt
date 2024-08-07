package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentSkillBinding

class SkillFragment : BaseFragment<FragmentSkillBinding>() {
    override val inflate: Inflate<FragmentSkillBinding>
        get() = FragmentSkillBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {

    }
}