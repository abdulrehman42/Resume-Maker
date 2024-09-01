package com.pentabit.cvmaker.resumebuilder.views.fragments.choose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnTemplateSelected
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentBasicBinding
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.views.activities.SubscriptionActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.TempResListAdpter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.ads_manager.ads_callback.RewardedLoadAndShowCallback
import com.pentabit.pentabitessentials.firebase.AppsKitSDK
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils


class BasicFragment(val list: List<TemplateModel>?, val screenId: ScreenIDs) :
    BaseFragment<FragmentBasicBinding>() {
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
                            if (it.contentType == 0 || it.contentType == 1 && AppsKitSDK.getInstance().removeAdsStatus) {
                                AppsKitSDKPreferencesManager.getInstance()
                                    .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
                                callback?.onTemplateSelected(it)
                            } else if (it.contentType == 1) {
                                DialogueBoxes.alertboxImport(currentActivity(),
                                    object :
                                        DialogueBoxes.StringValueDialogCallback {
                                        override fun onButtonClick(value: String) {
                                            if (value == Constants.YES) {
                                                AppsKitSDKAdsManager.loadAndShowRewardedAd(
                                                    currentActivity(),
                                                    object : RewardedLoadAndShowCallback {
                                                        override fun onAdFailed() {
                                                            AppsKitSDKUtils.makeToast("Ad failed to load")
                                                        }

                                                        override fun onAdRewarded() {
                                                            AppsKitSDKPreferencesManager.getInstance()
                                                                .addInPreferences(
                                                                    Constants.TEMPLATE_ID,
                                                                    it.id.toString()
                                                                )
                                                            callback?.onTemplateSelected(it)
                                                        }

                                                    }, Utils.createAdKeyFromScreenId(screenId)
                                                )

                                            }
                                        }

                                    })
                            } else {
                                startActivity(
                                    Intent(
                                        currentActivity(),
                                        SubscriptionActivity::class.java
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }

        templateAdapter.setOnItemClickCallback {
            if (it.contentType == 0 || it.contentType == 1 && AppsKitSDK.getInstance().removeAdsStatus) {
                AppsKitSDKPreferencesManager.getInstance()
                    .addInPreferences(Constants.TEMPLATE_ID, it.id.toString())
                callback?.onTemplateSelected(it)
            } else if (it.contentType == 1) {
                DialogueBoxes.alertboxImport(currentActivity(),
                    object :
                        DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.YES) {
                                AppsKitSDKAdsManager.loadAndShowRewardedAd(
                                    currentActivity(),
                                    object : RewardedLoadAndShowCallback {
                                        override fun onAdFailed() {
                                            AppsKitSDKUtils.makeToast("Ad failed to load")
                                        }

                                        override fun onAdRewarded() {
                                            AppsKitSDKPreferencesManager.getInstance()
                                                .addInPreferences(
                                                    Constants.TEMPLATE_ID,
                                                    it.id.toString()
                                                )
                                            callback?.onTemplateSelected(it)
                                        }
                                    }, Utils.createAdKeyFromScreenId(screenId)
                                )
                            }
                        }

                    })
            } else {
                startActivity(Intent(currentActivity(), SubscriptionActivity::class.java))
            }
        }

        binding.recyclerviewTemplete.apply {
            adapter = templateAdapter
        }
    }

}