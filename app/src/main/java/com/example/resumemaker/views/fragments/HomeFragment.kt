package com.example.resumemaker.views.fragments

import android.content.Intent
import android.os.Bundle
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentHomeBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes.alertboxPurchase
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.activities.DownloadActivity
import com.example.resumemaker.views.activities.ProfileActivity

class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    override val inflate: Inflate<FragmentHomeBinding>
        get() = FragmentHomeBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
    }

    private fun onclick() {
        binding.linearLayout1.setOnClickListener {
            sharePref.writeString(Constants.FRAGMENT_NAME,Constants.CV)
            requireActivity().startActivity(Intent(requireActivity(),ChoiceTemplate::class.java))
        }
        binding.linearLayout2.setOnClickListener {
            sharePref.writeString(Constants.FRAGMENT_NAME,Constants.COVERLETTER)
            requireActivity().startActivity(Intent(requireActivity(),ChoiceTemplate::class.java))

        }
        binding.linearLayout3.setOnClickListener {
            sharePref.writeString(Constants.FRAGMENT_NAME,Constants.DOWNLOAD)
            requireActivity().startActivity(Intent(requireActivity(),DownloadActivity::class.java))
        }
        binding.linearLayout4.setOnClickListener {
            val intent=Intent(currentActivity(),ChoiceTemplate::class.java)
            intent.putExtra(Constants.FRAGMENT_NAME,Constants.PROFILE)
            requireActivity().startActivity(intent)
        }
        binding.removeAddbtn.setOnClickListener {
            alertboxPurchase(currentActivity())
        }


    }


}