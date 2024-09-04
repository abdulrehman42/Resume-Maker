package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>(),AddLanguageFragment.OnLanguageUpdate {
    val singleStringAdapter = SingleStringAdapter()
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    var list = ArrayList<String>()
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userLanguages.isNullOrEmpty(), binding.popup)
            list = it.userLanguages as ArrayList<String>
            singleStringAdapter.submitList(list)
            setadapter()
        }

    }

    override fun csnMoveForward(): Boolean {
        return true
    }


    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty())
        {
            AppsKitSDKUtils.makeToast("please Add at least one language")
            return false

        }else{
            saveCallApi()
            return false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        fetchProfileData()
        onclick()
    }

    private fun fetchProfileData() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun setadapter() {
        singleStringAdapter.submitList(list)
        singleStringAdapter.setOnEditItemClickCallback { item, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddLanguageFragment(
                    list,
                    position,
                    this
                )
            )
        }
        singleStringAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){
                deleteItemPopup(currentActivity(), "Do you want to delete this language record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    singleStringAdapter.submitList(list)

                                }
                            }
                        }
                    })
            }else{
                AppsKitSDKUtils.makeToast("sorry at least one language required")

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
                    this
                )
            )
        }
    }

    override fun languageUpdate(languagelist: List<String>) {
        list = ArrayList(languagelist)
        AppsKitSDKUtils.setVisibility(languagelist.isEmpty(), binding.popup)
        singleStringAdapter.submitList(list)
        singleStringAdapter.notifyDataSetChanged()
    }
}