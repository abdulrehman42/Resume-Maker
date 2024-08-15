package com.example.resumemaker.views.fragments.choose

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentPremiumBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.activities.LoginActivity
import com.example.resumemaker.views.adapter.TemplateAdapter


class PremiumFragment : BaseFragment<FragmentPremiumBinding>() {
    override val inflate: Inflate<FragmentPremiumBinding>
        get() = FragmentPremiumBinding::inflate




    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
    }

    private fun setAdapter() {
        val fromCalled=sharePref.readString(Constants.FRAGMENT_NAME)

        val templateAdapter= TemplateAdapter(requireActivity(), Helper.getTemplateImages(),{
            if (fromCalled==Constants.CV)
            {
                startActivity(Intent(currentActivity(), LoginActivity::class.java))
//                currentActivity().replaceChoiceFragment(R.id.nav_profileFragment)
            }
        },{
            DialogueBoxes.alertbox(it.image, currentActivity(), object : DialogueBoxes.DialogCallback {
                override fun onButtonClick(isConfirmed: Boolean) {
                    // Handle the result here
                    if (isConfirmed) {
                        if (fromCalled== Constants.CV)
                        {
                            currentActivity().replaceChoiceFragment(R.id.nav_add_detail)
                        }else{
                            currentActivity().replaceChoiceFragment(R.id.nav_add_detail_coverletter)
                        }

                    }
                }
            })
        })
        binding.recyclerviewTemplete.apply {
            layoutManager= GridLayoutManager(requireActivity(),2)
            adapter = templateAdapter
        }
    }

}