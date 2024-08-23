package com.pentabit.cvmaker.resumebuilder.views.fragments.download

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentPdfBinding
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.views.adapter.TemplateAdapter


class PdfFragment : BaseFragment<FragmentPdfBinding>(){
    override val inflate: Inflate<FragmentPdfBinding>
        get() = FragmentPdfBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
    }
    private fun setAdapter() {

        val templateAdapter= TemplateAdapter(requireActivity(), Helper.getTemplateImages(),{},{

        })
        binding.recyclerviewTemplete.apply {
            layoutManager= GridLayoutManager(requireActivity(),2)
            adapter = templateAdapter
        }
    }

}