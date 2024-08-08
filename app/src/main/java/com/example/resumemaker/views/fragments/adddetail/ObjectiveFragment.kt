package com.example.resumemaker.views.fragments.adddetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentObjectiveBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.ObjSampleAdapter
import com.google.android.material.tabs.TabLayout


class ObjectiveFragment : BaseFragment<FragmentObjectiveBinding>() {
    lateinit var objSampleAdapter: ObjSampleAdapter
    override val inflate: Inflate<FragmentObjectiveBinding>
        get() = FragmentObjectiveBinding::inflate

    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
        onclick()

    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout

        binding.viewall.setOnClickListener {
            objSampleAdapter.updateMaxItemCount(Helper.getSampleList().size)
        }
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(0)!!.select()
        }
        binding.nextbtn.setOnClickListener {
            tabhost.getTabAt(2)!!.select()
        }
    }

    private fun setAdapter() {

        objSampleAdapter=ObjSampleAdapter(currentActivity(),Helper.getSampleList()){
            binding.objectiveTextInput.setText(it)
        }
        binding.sampleRecyclerview.adapter=objSampleAdapter
    }
}