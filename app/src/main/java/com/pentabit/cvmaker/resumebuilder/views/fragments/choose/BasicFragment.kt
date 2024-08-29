package com.pentabit.cvmaker.resumebuilder.views.fragments.choose

import android.content.Context
import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnTemplateSelected
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentBasicBinding
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.views.adapter.TempResListAdpter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.ads_manager.ads_callback.RewardedAdCallbacks
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager


class BasicFragment(val list: List<TemplateModel>?) : BaseFragment<FragmentBasicBinding>() {
    private val templateAdapter = TempResListAdpter()
    private var callback: OnTemplateSelected? = null
    override val inflate: Inflate<FragmentBasicBinding>
        get() = FragmentBasicBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        setAdapter()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTemplateSelected) {
            callback = context
        }
    }

    override fun observeLiveData() {

    }

    private fun setAdapter() {
        templateAdapter.submitList(list)
        templateAdapter.setOnEyeItemClickCallback {
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
            DialogueBoxes.alertbox(
                it.path,
                currentActivity(),
                object : DialogueBoxes.DialogCallback {
                    override fun onButtonClick(isConfirmed: Boolean) {
                        if (isConfirmed) {
                            if (it.contentType == 1) {
                                DialogueBoxes.alertboxImport(currentActivity(),
                                    object :
                                        DialogueBoxes.StringValueDialogCallback {
                                        override fun onButtonClick(value: String) {
                                            if (value == Constants.YES) {
                                                AppsKitSDKAdsManager.showRewarded(
                                                    currentActivity(),
                                                    "",
                                                    object : RewardedAdCallbacks {
                                                        override fun onRewardedCompleted() {
                                                            AppsKitSDKPreferencesManager.getInstance()
                                                                .addInPreferences(
                                                                    Constants.TEMPLATE_ID,
                                                                    it.id.toString()
                                                                )
                                                            callback?.onTemplateSelected(it)

                                                        }

                                                        override fun onAdRewarded() {
                                                            AppsKitSDKPreferencesManager.getInstance()
                                                                .addInPreferences(
                                                                    Constants.TEMPLATE_ID,
                                                                    it.id.toString()
                                                                )
                                                            callback?.onTemplateSelected(it)
                                                        }

                                                        override fun onAdDismissed() {

                                                        }

                                                        override fun onRewardedFailedToShow() {
                                                            currentActivity().showToast("ad failed")
                                                        }

                                                        override fun onRewardedLoaded() {
                                                        }

                                                        override fun onLoadFailure() {
                                                            currentActivity().showToast("ad failed")
                                                        }

                                                    })

                                            }
                                        }

                                    })
                            } else {
                                AppsKitSDKPreferencesManager.getInstance()
                                    .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
                                callback?.onTemplateSelected(it)
                            }
                        } else {
                            AppsKitSDKPreferencesManager.getInstance()
                                .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
                            callback?.onTemplateSelected(it)
                        }
                    }
                }
            )
        }

        templateAdapter.setOnItemClickCallback {
            if (it.contentType == 1) {
                DialogueBoxes.alertboxImport(currentActivity(),
                    object :
                        DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.YES) {
                                AppsKitSDKAdsManager.showRewarded(
                                    currentActivity(),
                                    "",
                                    object : RewardedAdCallbacks {
                                        override fun onRewardedCompleted() {
                                            AppsKitSDKPreferencesManager.getInstance()
                                                .addInPreferences(
                                                    Constants.TEMPLATE_ID,
                                                    it.id.toString()
                                                )
                                            callback?.onTemplateSelected(it)

                                        }

                                        override fun onAdRewarded() {
                                            AppsKitSDKPreferencesManager.getInstance()
                                                .addInPreferences(
                                                    Constants.TEMPLATE_ID,
                                                    it.id.toString()
                                                )
                                            callback?.onTemplateSelected(it)
                                        }

                                        override fun onAdDismissed() {

                                        }

                                        override fun onRewardedFailedToShow() {
                                            currentActivity().showToast("ad failed")
                                        }

                                        override fun onRewardedLoaded() {
                                        }

                                        override fun onLoadFailure() {
                                            currentActivity().showToast("ad failed")
                                        }

                                    })

                            }
                        }

                    })
            } else {
                AppsKitSDKPreferencesManager.getInstance()
                    .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
                callback?.onTemplateSelected(it)
            }

        }

        binding.recyclerviewTemplete.apply {
            adapter = templateAdapter
        }
    }

}