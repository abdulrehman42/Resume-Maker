package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentInterestBinding

class InterestFragment : BaseFragment<FragmentInterestBinding>() {
    override val inflate: Inflate<FragmentInterestBinding>
        get() = FragmentInterestBinding::inflate

    override fun observeLiveData() {
     }

    override fun init(savedInstanceState: Bundle?) {
     }
}