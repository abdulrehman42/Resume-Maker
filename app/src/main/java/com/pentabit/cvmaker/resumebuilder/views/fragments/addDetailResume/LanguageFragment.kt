package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

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
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.SingleStringAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    val singleStringAdapter = SingleStringAdapter()
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list=ArrayList<String>()
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            list= it.userLanguages as ArrayList<String>
            setadapter(list)
        }
        addDetailResumeVM.languageResponse.observe(viewLifecycleOwner){
            apiCall()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        AppsKitSDKAdsManager.showNative(currentActivity(),binding.bannerAdd,""

        );
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString())
    }

    private fun setadapter(userLanguages: List<String>) {
        singleStringAdapter.submitList(userLanguages)
        singleStringAdapter.setOnEditItemClickCallback {
            sharePref.writeString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA, it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddLanguageFragment()
        }
        singleStringAdapter.setOnItemDeleteClickCallback {
            list.removeAt(it)
            if (list.size!=0)
            {
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
            sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString(),
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
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()
            }

        }
        binding.addlanguage.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddLanguageFragment()
        }
    }
}