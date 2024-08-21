package com.example.resumemaker.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentProfileBinding
import com.example.resumemaker.models.api.ProfileListingModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.DialogueBoxes
import com.example.resumemaker.viewmodels.ProfileVM
import com.example.resumemaker.views.activities.AddDetailResume
import com.example.resumemaker.views.adapter.ProfileAdapter
import com.example.resumemaker.views.fragments.addDetailResume.ResumePreviewFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    lateinit var profileVM: ProfileVM
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    override fun observeLiveData() {
        profileVM.dataResponse.observe(currentActivity()) {
            setadapter(it)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        profileVM = ViewModelProvider(currentActivity())[ProfileVM::class.java]
        apiCall()
        onclick()
        binding.includeTool.textView.text = getString(R.string.profile)
    }

    private fun apiCall() {
        profileVM.getProfileList()
    }

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()
        }

        binding.addTabs.setOnClickListener {
            DialogueBoxes.alertboxChooseCreate(
                currentActivity(),
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        when (value) {
                            Constants.CREATE -> startActivity(
                                Intent(
                                    currentActivity(),
                                    AddDetailResume::class.java
                                )
                            )

                            Constants.IMPORT -> DialogueBoxes.alertboxImport(currentActivity())
                        }

                    }
                })
        }

    }

    private fun setadapter(profileModels: List<ProfileListingModel>) {
        val profileAdapter = ProfileAdapter(currentActivity(),profileModels)
        {
            val fromCalled = currentActivity().intent.getStringExtra(Constants.IS_RESUME)
            if (fromCalled == Constants.PROFILE) {
                sharePref.writeString(Constants.PROFILE_ID,it.id.toString())
                currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
            } else {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.profileHostFragment, ResumePreviewFragment())
                    .addToBackStack(null)
                    .commit()
            }

        }
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)

            adapter = profileAdapter
        }
    }

}