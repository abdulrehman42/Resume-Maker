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
            startActivity(Intent(currentActivity(),ChoiceTemplate::class.java).putExtra(Constants.IS_RESUME, true))
        }

        binding.linearLayout2.setOnClickListener {
            startActivity(Intent(currentActivity(),ChoiceTemplate::class.java).putExtra(Constants.IS_RESUME, false))

        }
        binding.linearLayout3.setOnClickListener {
            sharePref.writeString(Constants.IS_RESUME,Constants.DOWNLOAD)
            startActivity(Intent(requireActivity(),DownloadActivity::class.java))
        }
        binding.linearLayout4.setOnClickListener {
            val intent=Intent(currentActivity(),ProfileActivity::class.java)
            intent.putExtra(Constants.IS_RESUME,Constants.PROFILE)
            startActivity(intent)
        }
        binding.removeAddbtn.setOnClickListener {
            alertboxPurchase(currentActivity())
        }


    }


}