package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLanguageFragment(val data: List<String>?, val language: String?, val isedit: Boolean, val position: Int) :
    BaseFragment<FragmentAddLanguageBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var list = ArrayList<String>()
    var looksAdapter= LooksAdapter()
    private val userlanguasAdapter = UserSkillAdapter()
    var startWord="-1__"
    var isselected=false
    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.languageResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
        addDetailResumeVM.looksupResponse.observe(this) {
            looksAdapter.submitList(it)
            binding.lookidRecyclerview.isGone=false
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
//        val data_list=sharePref.readProfileData()
        binding.includeTool.textView.text = getString(R.string.add_language)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        if (data != null) {
            list = data as ArrayList<String>
        }
        language?.let {
            binding.languageEdittext.setText(language)
        }
        binding.recyclerviewLanguage.adapter=userlanguasAdapter
        binding.lookidRecyclerview.adapter = looksAdapter
        looksAdapter.setOnItemClickCallback {
            startWord="1__"
            binding.languageEdittext.setText(Helper.removeOneUnderscores(it.text))
            binding.lookidRecyclerview.isGone = true
            isselected=true
        }
        onclick()


    }

    @SuppressLint("NotifyDataSetChanged")
        private fun onclick() {
            binding.languageEdittext.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No action needed here
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val textLength = s?.length ?: 0
                    if (textLength > 2) {
                        binding.tickBtn.isGone=false
                    }
                    else{
                        binding.tickBtn.isGone=true
                    }
                    val query = s.toString()
                    if (!isselected)
                    {
                        callLookUpApi(query)
                    }
                    if (query.isNullOrEmpty())
                    {
                        binding.lookidRecyclerview.isGone=true
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    val query = s.toString()
                    if (!isselected)
                    {
                        callLookUpApi(query)
                    }

                }

            })

        binding.tickBtn.setOnClickListener {
            val skill = binding.languageEdittext.text.toString().trim()
            if (skill.isNotEmpty()) {
                if (!isedit)
                {
                    list.add(startWord + skill)
                }else{
                    list[position]=startWord+skill
                }
                binding.languageEdittext.setText("")
                userlanguasAdapter.submitList(list.toList())
            }
            binding.lookidRecyclerview.isGone=true
        }

            binding.savebtn.setOnClickListener {
                if (list.isNotEmpty()) {
                    apiCall()
                } else {
                    AppsKitSDKUtils.makeToast("please add language")
                }
            }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

    }

    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.languages,query,"","6")

    }


    private fun apiCall() {
        addDetailResumeVM.editLanguage(
            LanguageRequestModel(list)
        )
    }

}
