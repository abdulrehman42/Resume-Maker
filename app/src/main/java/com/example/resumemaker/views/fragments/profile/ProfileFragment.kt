package com.example.resumemaker.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProfileBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.activities.ChoiceTemplate
import com.example.resumemaker.views.adapter.ProfileAdapter


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    override fun observeLiveData() {
    }

    override fun init(savedInstanceState: Bundle?) {
        onclick()
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()
        }
        binding.includeTool.textView.setText("Profile")
        setadapter()
    }

    private fun onclick() {
        binding.addTabs.setOnClickListener {
            DialogueBoxes.alertboxChooseCreate(
                currentActivity(),
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        when(value)
                        {
                            Constants.CREATE->startActivity(Intent(currentActivity(),AddDetailResume::class.java))
                            Constants.IMPORT->DialogueBoxes.alertboxImport(currentActivity())
                        }

                    }
                })
        }

    }

    private fun setadapter() {
        val profileAdapter=ProfileAdapter(currentActivity(),Helper.getProfileList1())
        {
            val fromCalled=currentActivity().intent.getStringExtra(Constants.FRAGMENT_NAME)

            if (fromCalled==Constants.PROFILE){
                sharePref.writeData(it)
                currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
            }else{
                val intent= Intent(currentActivity(),ChoiceTemplate::class.java)
                intent.putExtra(Constants.FRAGMENT_NAME,Constants.PROFILE)
                startActivity(intent)


            }

        }
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)

            adapter=profileAdapter
        }
    }

}