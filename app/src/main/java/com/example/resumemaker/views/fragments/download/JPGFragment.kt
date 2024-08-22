package com.example.resumemaker.views.fragments.download

import android.os.Bundle
import androidx.core.view.isGone
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentJPGBinding
import com.example.resumemaker.utils.Helper.getImagesFromResumeMakerFolder
import com.example.resumemaker.views.adapter.download.DownloadImgAdapter


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
        if (getImagesFromResumeMakerFolder(currentActivity()).size==0)
        {
            binding.text.isGone=false
        }else{
            binding.text.isGone=true
        }
        val templateAdapter= DownloadImgAdapter(getImagesFromResumeMakerFolder(currentActivity()))
        binding.recyclerviewTemplete.apply {
            layoutManager= GridLayoutManager(requireActivity(),2)
            adapter = templateAdapter
        }
    }


}