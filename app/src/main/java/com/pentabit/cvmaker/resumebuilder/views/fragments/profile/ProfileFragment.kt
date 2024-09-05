package com.pentabit.cvmaker.resumebuilder.views.fragments.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.activityViewModels
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
import com.pentabit.cvmaker.resumebuilder.views.activities.ResumePreviewActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.ProfileAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.ads_manager.ads_callback.RewardedLoadAndShowCallback
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {
    private val profileVM: ProfileVM by activityViewModels()
    private val screeId = ScreenIDs.PROFILE_LISTING
    private var isCreateResume = false
    override val inflate: Inflate<FragmentProfileBinding>
        get() = FragmentProfileBinding::inflate

    private val profileAdapter = ProfileAdapter()

    override fun observeLiveData() {
        profileVM.loadingState.observe(currentActivity()) {
            AppsKitSDKUtils.setVisibility(it, binding.loader)
        }
        profileVM.getString.observe(currentActivity()) {
            fetchProfileData()
        }
        profileVM.dataResponse.observe(currentActivity()) {
            AppsKitSDKUtils.setVisibility(
                it.isNullOrEmpty(), binding.popupmsg, binding.addTabshide, binding.addTabs
            )

            profileAdapter.submitList(it)
            profileAdapter.notifyDataSetChanged()

            if (isCreateResume && it.isNullOrEmpty()
            ) {
                startActivity(
                    Intent(
                        currentActivity(),
                        AddDetailResume::class.java
                    ).putExtra("CreateResume", true)
                )
                currentActivity().finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screeId)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.profile)
        isCreateResume = AppsKitSDKPreferencesManager.getInstance()
            .getStringPreferences(Constants.TEMPLATE_ID).isNotEmpty()
        profileAdapter.isViewProfile = !isCreateResume
        setAdapter()
        onclick()
        handleAds()
    }


    private fun handleAds() {
        AppsKitSDKAdsManager.showBanner(
            requireActivity(), binding.banner, Utils.createAdKeyFromScreenId(screeId)
        )
    }

    private fun fetchProfileData() {
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
            onCreateProfileClicked()
        }
    }

    private fun onCreateProfileClicked() {
        if (profileAdapter.currentList.isNotEmpty()) {
            AppsKitSDKAdsManager.loadAndShowRewardedAd(
                requireActivity(), object : RewardedLoadAndShowCallback {
                    override fun onAdFailed() {
                        AppsKitSDKUtils.makeToast("Ad not available")
                    }

                    override fun onAdRewarded() {
                        startActivity(
                            Intent(
                                currentActivity(), AddDetailResume::class.java
                            )
                        )
                    }

                }, Utils.createAdKeyFromScreenId(screeId)
            )
        } else {
            startActivity(
                Intent(
                    currentActivity(), AddDetailResume::class.java
                )
            )
        }
    }

    private fun setAdapter() {
        profileAdapter.setCallback(object : ProfileItemCallbacks {
            override fun onItemClicked(id: String) {
                AppsKitSDKPreferencesManager.getInstance()
                    .addInPreferences(Constants.PROFILE_ID, id)
                if (!isCreateResume) {
                    viewModelStore
                    currentActivity().replaceProfileFragment(R.id.nav_profileDetailFragment)
                } else {
                    val intent = Intent(currentActivity(), ResumePreviewActivity::class.java)
                    intent.putExtra(Constants.IS_RESUME, true)
                    startActivity(intent)
                    currentActivity().finish()
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

