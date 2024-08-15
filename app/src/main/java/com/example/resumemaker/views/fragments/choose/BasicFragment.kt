package com.example.resumemaker.views.fragments.choose

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentBasicBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.activities.BoardingScreen
import com.example.resumemaker.views.activities.LoginActivity
import com.example.resumemaker.views.adapter.TemplateAdapter

class BasicFragment : BaseFragment<FragmentBasicBinding>() {
    lateinit var fromCalled:String
    override val inflate: Inflate<FragmentBasicBinding>
        get() = FragmentBasicBinding::inflate



    override fun observeLiveData() {

    }

    override fun init(savedInstanceState: Bundle?) {
        fromCalled= sharePref.readString(Constants.FRAGMENT_NAME).toString()
        setAdapter()
    }



    private fun setAdapter() {

        val templateAdapter= TemplateAdapter(requireActivity(), Helper.getTemplateImages(),{
            if (fromCalled==Constants.CV)
            {
                sharePref.writeString(Constants.FRAGMENT_NAME,Constants.CHOSE_TEMPLATE)
                startActivity(Intent(currentActivity(), LoginActivity::class.java))
                //currentActivity().replaceChoiceFragment(R.id.nav_profileFragment)
            }
            if (fromCalled==Constants.PROFILE)
            {

                //startActivity(Intent(currentActivity(), LoginActivity::class.java))
                currentActivity().replaceChoiceFragment(R.id.nav_profileFragment)
            }
        },{

            DialogueBoxes.alertbox(it.image, currentActivity(), object : DialogueBoxes.DialogCallback {
                override fun onButtonClick(isConfirmed: Boolean) {
                    // Handle the result here
                    if (isConfirmed) {
                        if (fromCalled== Constants.CV) {
                            currentActivity().replaceChoiceFragment(R.id.nav_add_detail)
                        }
                        else{
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