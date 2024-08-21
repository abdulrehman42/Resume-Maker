package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.AddDetailsBaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentLanguageBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.SkillAdapter
import com.example.resumemaker.views.adapter.adddetailresume.LanguageAdapter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LanguageFragment : AddDetailsBaseFragment<FragmentLanguageBinding>() {
    lateinit var languageAdapter: LanguageAdapter
    lateinit var addDetailResumeVM: AddDetailResumeVM
    override val inflate: Inflate<FragmentLanguageBinding>
        get() = FragmentLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            setadapter(it.userLanguages)
        }
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(requireActivity())[AddDetailResumeVM::class.java]
        apiCall()
        onclick()
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail(sharePref.readString(Constants.PROFILE_ID).toString())
    }

    private fun setadapter(userLanguages: List<String>) {
       languageAdapter.submitList(userLanguages)
        languageAdapter.setOnEditItemClickCallback {
            callDeleteApi()
        }
        languageAdapter.setOnItemDeleteClickCallback {
            sharePref.writeString(Constants.DATA,it)
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddLanguageFragment()
        }
        binding.recyclerviewLanguage.apply {
            adapter = languageAdapter
        }
    }

    private fun callDeleteApi() {

    }

    private fun onclick() {
        val tabhost = currentActivity().findViewById<View>(R.id.tab_layout_adddetail) as TabLayout
        binding.backbtn.setOnClickListener {
            tabhost.getTabAt(6)!!.select()

        }
        binding.nextbtn.setOnClickListener {
            if (tabhost.tabCount >= 8) {
                tabhost.getTabAt(8)!!.select()
            }else{
                addDetailResumeVM.isHide.value = false
                addDetailResumeVM.fragment.value = ResumePreviewFragment()
            }

        }
        binding.addlanguage.setOnClickListener {
            addDetailResumeVM.isHide.value = false
            addDetailResumeVM.fragment.value = AddEducation()
        }
    }
}