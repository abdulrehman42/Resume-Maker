package com.example.resumemaker.views.fragments.profile

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProfileBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.ProfileAdapter


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()
        }
        binding.includeTool.textView.setText("Profile")
        setadapter()
    }

    private fun setadapter() {
        val profileAdapter=ProfileAdapter(currentActivity(),Helper.getProfileList1())
        {
            sharePref.writeData(it)
            currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
        }
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)

            adapter=profileAdapter
        }
    }

}