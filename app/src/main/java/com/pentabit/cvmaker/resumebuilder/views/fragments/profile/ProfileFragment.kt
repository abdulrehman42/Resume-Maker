package com.pentabit.cvmaker.resumebuilder.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProfileBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import com.pentabit.cvmaker.resumebuilder.viewmodels.ProfileVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.ProfileAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ResumePreviewFragment
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    lateinit var profileVM: ProfileVM
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    override fun observeLiveData() {
        profileVM.dataResponse.observe(currentActivity()) {
            AppsKitSDKUtils.setVisibility(it.isNullOrEmpty(), binding.popupmsg)
            setadapter(it)
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        profileVM = ViewModelProvider(currentActivity())[ProfileVM::class.java]
        binding.includeTool.textView.text = getString(R.string.profile)
        apiCall()
        onclick()
    }

    private fun apiCall() {
        profileVM.getProfileList()
    }

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()
        }

        binding.addTabs.setOnClickListener {
            startActivity(
                Intent(
                    currentActivity(),
                    AddDetailResume::class.java
                )
            )
            /*DialogueBoxes.alertboxChooseCreate(
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
                })*/
        }

    }

    private fun setadapter(profileModels: List<ProfileListingModel>) {
        val profileAdapter = ProfileAdapter(currentActivity(), profileModels)
        {
            val fromCalled =
                sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.FRAGMENT_CALLED)
            if (fromCalled == com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE) {
                sharePref.writeString(
                    com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID,
                    it.id.toString()
                )
                currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
            } else {
                sharePref.writeString(
                    com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID,
                    it.id.toString()
                )
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