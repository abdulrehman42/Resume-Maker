package com.pentabit.cvmaker.resumebuilder.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.ProfileItemCallbacks
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProfileBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseProfile
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.ProfileVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.ProfileAdapter
import com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume.ResumePreviewFragment
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.ads_manager.ads_callback.RewardedLoadAndShowCallback
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    lateinit var profileVM: ProfileVM
    private val screeId = ScreenIDs.PROFILE_LISTING
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
                startActivity(Intent(currentActivity(), AddDetailResume::class.java))
                binding.addTabshide.isGone = false
                binding.popupmsg.isGone = false
                binding.addTabs.isGone = true
            } else {
                binding.popupmsg.isGone = true
                binding.addTabshide.isGone = true
                binding.addTabs.isGone = false
                profileAdapter.submitList(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screeId)
    }

    override fun init(savedInstanceState: Bundle?) {
        profileVM = ViewModelProvider(currentActivity())[ProfileVM::class.java]
        binding.includeTool.textView.text = getString(R.string.profile)
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.VIEW_PROFILE)
        ) {
            profileAdapter.isViewProfile = true
        }
        setadapter()
        apiCall()
        onclick()
        handleAds()
    }


    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            requireActivity(),
            binding.banner,
            Utils.createAdKeyFromScreenId(screeId)
        )
    }

    private fun apiCall() {
        profileVM.getProfileList()
    }

    private fun onclick() {

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().finish()
        }

        binding.addTabs.setOnClickListener {
            onCreateProfileClicked()
        }

        binding.addTabshide.setOnClickListener {
            startActivity(
                Intent(
                    currentActivity(),
                    AddDetailResume::class.java
                )
            )
        }
    }

    private fun onCreateProfileClicked() {
        if (profileAdapter.currentList.isNotEmpty()) {
            AppsKitSDKAdsManager.loadAndShowRewardedAd(
                requireActivity(),
                object : RewardedLoadAndShowCallback {
                    override fun onAdFailed() {
                        AppsKitSDKUtils.makeToast("Ad not available")
                    }

                    override fun onAdRewarded() {
                        startActivity(
                            Intent(
                                currentActivity(),
                                AddDetailResume::class.java
                            )
                        )
                    }

                },
                Utils.createAdKeyFromScreenId(screeId)
            )
        } else {
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

    override fun onDestroy() {
        super.onDestroy()
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.VIEW_PROFILE, false)
    }
}

