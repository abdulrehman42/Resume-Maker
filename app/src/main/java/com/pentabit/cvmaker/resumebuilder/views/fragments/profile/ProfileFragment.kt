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
import com.pentabit.cvmaker.resumebuilder.utils.Constants
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
        profileVM.loadingState.observe(currentActivity()) {
            if (it) {
                binding.loader.isGone = false
            } else {
                binding.loader.isGone = true
            }
        }
        profileVM.getString.observe(currentActivity()) {
            apiCall()
        }
        profileVM.dataResponse.observe(currentActivity()) {
            if (it.size == 0) {
                binding.popupmsg.isGone = false
            } else {
                binding.popupmsg.isGone = true
                setadapter(it)

            }
            // AppsKitSDKUtils.setVisibility(it.isNullOrEmpty(), binding.popupmsg)
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

        }

    }

    private fun setadapter(profileModels: List<ProfileListingModel>) {
        val profileAdapter = ProfileAdapter(currentActivity(), profileModels, {
            val fromCalled =
                sharePref.readString(Constants.FRAGMENT_CALLED)
            if (fromCalled == Constants.PROFILE) {
                sharePref.writeString(
                    Constants.PROFILE_ID,
                    it.toString()
                )
                currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
            } else {
                sharePref.writeString(
                    Constants.PROFILE_ID,
                    it.toString()
                )
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.profileHostFragment, ResumePreviewFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }, {
            profileVM.onDeleteProfile(it.toString())
        }, { profile_id ->
            val intent = Intent(requireActivity(), AddDetailResume::class.java)
            intent.putExtra(Constants.CALL_API, profile_id)
            startActivity(intent)

        })
        binding.recyclerview.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)

            adapter = profileAdapter
        }
    }
}

