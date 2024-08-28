package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATION_TIME
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseCreation
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.ChoiceTemplate
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    val singleStringAdapter = SingleStringAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<String>()
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isEmpty(),binding.popup)
            list = it.userLanguages as ArrayList<String>
            setadapter(list)
        }

        addDetailResumeVM.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showNative(
            currentActivity(), binding.bannerAdd, ""

        )
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter(userLanguages: List<String>) {
        singleStringAdapter.submitList(userLanguages)
        singleStringAdapter.setOnEditItemClickCallback {
            addDetailResumeVM.fragment.value = AddLanguageFragment(userLanguages, it)
        }
        singleStringAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            if (list.size != 0) {
                saveCallApi()
                apiCall()

            }
        }
        binding.recyclerviewLanguage.apply {
            adapter = singleStringAdapter
        }
    }

    private fun saveCallApi() {
        addDetailResumeVM.editLanguage(
            LanguageRequestModel(list)
        )
    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(6)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 8) {
                tabhost.getTabAt(8)!!.select()
            } else {
                alertboxChooseCreation(requireActivity(),
                    object : DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.PROFILE) {
                                AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.VIEW_PROFILE,true)
                                currentActivity().finish()
                            } else {
                                val intent = Intent(currentActivity(), ChoiceTemplate::class.java)
                                intent.putExtra(IS_RESUME,true)
                                intent.putExtra(CREATION_TIME, true)
                                startActivity(intent)
                                currentActivity().finish()
                            }
                        }

                    })            }

        }
        binding.addlanguage.setOnClickListener {
            addDetailResumeVM.fragment.value = AddLanguageFragment(list, null)
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
}