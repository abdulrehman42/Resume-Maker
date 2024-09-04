package com.pentabit.cvmaker.resumebuilder.views.fragments.coverletter

import android.annotation.SuppressLint
import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentSampleDetailBinding


class SampleDetail : BaseFragment<FragmentSampleDetailBinding>() {
    override val inflate: Inflate<FragmentSampleDetailBinding>
        get() = FragmentSampleDetailBinding::inflate

    override fun observeLiveData() {

    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {

    }

}