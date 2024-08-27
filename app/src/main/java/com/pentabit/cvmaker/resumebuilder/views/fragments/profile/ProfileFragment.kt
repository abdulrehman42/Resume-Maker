package com.pentabit.cvmaker.resumebuilder.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.ProfileItemCallbacks
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProfileBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseProfile
import com.pentabit.cvmaker.resumebuilder.viewmodels.ProfileVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.ProfileAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ResumePreviewFragment
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    lateinit var profileVM: ProfileVM
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    val profileAdapter = ProfileAdapter()

    override fun observeLiveData() {
        profileVM.loadingState.observe(currentActivity()) {
            AppsKitSDKUtils.setVisibility(it, binding.loader)
        }
        profileVM.getString.observe(currentActivity()) {
            apiCall()
        }
        profileVM.dataResponse.observe(currentActivity()) {
            if (it.isNullOrEmpty()) {
                binding.popupmsg.isGone = false
            } else {
                binding.popupmsg.isGone = true
                profileAdapter.submitList(it)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        profileVM = ViewModelProvider(currentActivity())[ProfileVM::class.java]
        binding.includeTool.textView.text = getString(R.string.profile)
        setadapter()
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
        }

    }

    private fun setadapter() {
        profileAdapter.setCallback(object : ProfileItemCallbacks {
            override fun onItemClicked(id: String) {
                val fromCalled =
                    AppsKitSDKPreferencesManager.getInstance()
                        .getStringPreferences(Constants.FRAGMENT_CALLED)
                AppsKitSDKPreferencesManager.getInstance()
                    .addInPreferences(Constants.PROFILE_ID, id)
                if (fromCalled == Constants.PROFILE) {
                    viewModelStore
                    currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
                } else {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.profileHostFragment, ResumePreviewFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }

            override fun onOptionsClicked(id: String) {
                alertboxChooseProfile(requireActivity(),
                    object : DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.EDIT) {
                                AppsKitSDKPreferencesManager.getInstance()
                                    .addInPreferences(Constants.PROFILE_ID, id)
                                val intent = Intent(requireActivity(), AddDetailResume::class.java)
                                intent.putExtra(Constants.IS_EDIT, true)
                                startActivity(intent)
                            } else {
                                profileVM.onDeleteProfile(id)
                            }
                        }

                    })
            }
        })
        binding.recyclerview.apply {
            adapter = profileAdapter
        }
    }
}

