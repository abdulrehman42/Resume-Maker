package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    val singleStringAdapter = SingleStringAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<String>()
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userLanguages.isNullOrEmpty(), binding.popup)
            list = it.userLanguages as ArrayList<String>
            setadapter(list)
        }

    }

    override fun csnMoveForward(): Boolean {
        return true
    }


    override fun onMoveNextClicked(): Boolean {
        return true
    }

    private fun check(): Boolean {
        if (list.isNotEmpty()) {
            return true
        } else {
            AppsKitSDKUtils.makeToast("please add at least one language")
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
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
        singleStringAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddLanguageFragment(
                    userLanguages,
                    item,
                    true,
                    position
                )
            )
        }
        singleStringAdapter.setOnItemDeleteClickCallback {
            if (list.size != 0) {
                list.removeAt(it)
            }
            //setadapter(list)
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
        binding.addlanguage.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddLanguageFragment(
                    list,
                    null,
                    false,
                    0
                )
            )
        }
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }
}