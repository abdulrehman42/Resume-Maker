package com.example.resumemaker.views.fragments.download

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentJPGBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.TemplateAdapter


class JPGFragment : BaseFragment<FragmentJPGBinding>()
{
    override val inflate: Inflate<FragmentJPGBinding>
        get() = FragmentJPGBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()

    }

    private fun setAdapter() {

        val templateAdapter= TemplateAdapter(requireActivity(), Helper.getTemplateImages()){

        }
        binding.recyclerviewTemplete.apply {
            layoutManager= GridLayoutManager(requireActivity(),2)
            adapter = templateAdapter
        }
    }


}