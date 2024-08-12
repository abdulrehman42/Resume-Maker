package com.example.resumemaker.views.fragments.choose

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.AddmorealertdialogueBinding
import com.example.resumemaker.databinding.FragmentBasicBinding
import com.example.resumemaker.databinding.TemplateSelectLayoutBinding
import com.example.resumemaker.models.TablayoutModel
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.TemplateAdapter
import com.example.resumemaker.views.fragments.adddetail.AchievementFragment
import com.example.resumemaker.views.fragments.adddetail.InterestFragment
import com.example.resumemaker.views.fragments.adddetail.LanguageFragment
import com.example.resumemaker.views.fragments.adddetail.ProjectFragment

class BasicFragment : BaseFragment<FragmentBasicBinding>() {
    override val inflate: Inflate<FragmentBasicBinding>
        get() = FragmentBasicBinding::inflate



    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        /*binding.text.setOnClickListener {
            currentActivity().replaceChoiceFragment(R.id.nav_add_detail)
        }*/
        val templateAdapter=TemplateAdapter(requireActivity(),Helper.getTemplateImages()){
            if (DialogueBoxes.alertbox(it.image,currentActivity()))
            {
                currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter)
            }
        }
        binding.recyclerviewTemplete.apply {
            layoutManager=GridLayoutManager(requireActivity(),2)
            adapter = templateAdapter
        }

    }



}