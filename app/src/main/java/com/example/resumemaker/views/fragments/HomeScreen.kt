package com.example.resumemaker.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentHome2Binding
import com.example.resumemaker.databinding.FragmentHomeBinding

class HomeScreen : BaseFragment<FragmentHome2Binding>() {
    override val inflate: Inflate<FragmentHome2Binding>
        get() = FragmentHome2Binding::inflate

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home2, container, false)
    }

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {

    }


}