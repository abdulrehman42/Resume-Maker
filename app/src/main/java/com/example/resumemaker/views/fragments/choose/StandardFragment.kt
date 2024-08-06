package com.example.resumemaker.views.fragments.choose

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentStandardBinding


class StandardFragment : BaseFragment<FragmentStandardBinding>() {
    override val inflate: Inflate<FragmentStandardBinding>
        get() = FragmentStandardBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {

    }

}