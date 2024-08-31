package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddInterestFragment(
    val userInterests: List<String>?,
    val interest_name: String?,
    val isedit: Boolean,
    val position: Int
) : BaseFragment<FragmentAddInterestBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var looksAdapter = LooksAdapter()
    private val userlanguasAdapter = UserSkillAdapter()
    var startWord = "-1__"
    var isselected = false
    var oldList = ArrayList<String>()
    val updateList = ArrayList<String>()

//    var interests = ArrayList<String>()


    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.interestResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner) {
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }

        addDetailResumeVM.looksupResponse.observe(this) {
            looksAdapter.submitList(it)
            binding.lookidRecyclerview.isGone = false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_interest)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        userInterests?.let {
            oldList = userInterests as ArrayList<String>
            for (i in 0 until oldList.size) {
                updateList.add(startWord+Helper.removeOneUnderscores(oldList[i]))
            }
        }
        if (interest_name != null) {
            binding.interestEdittext.setText(interest_name)
        }
        binding.lookidRecyclerview.adapter = looksAdapter
        binding.recyclerviewSuggestions.adapter = userlanguasAdapter
        onclick()

    }


    private fun onclick() {

        looksAdapter.setOnItemClickCallback {
            startWord = "1__"
            binding.interestEdittext.setText(Helper.removeOneUnderscores(it.text))
            binding.lookidRecyclerview.isGone = true
            isselected = true
        }
        binding.savebtn.setOnClickListener {
            if (isCondtionMet()) {
                if (!isedit) {
                    for (i in 0 until oldList.size)
                    {
                        updateList.add(startWord + oldList[i])

                    }
                } else {

                    updateList[position] = startWord + binding.interestEdittext.text.toString()
                }

                apiCall()

            }
        }
        binding.tickBtn.setOnClickListener {
            val skill = binding.interestEdittext.text.toString().trim()
            if (skill.isNotEmpty()) {
                if (!isedit) {
                    updateList.add(startWord + skill)
                } else {
                    updateList[position] = startWord + skill
                }
                binding.interestEdittext.setText("")
                userlanguasAdapter.submitList(updateList.toList())
            }
            binding.lookidRecyclerview.isGone = true
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        currentActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        binding.interestEdittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val textLength = s?.length ?: 0
                if (textLength > 2) {
                    binding.tickBtn.isGone = false
                } else {
                    binding.tickBtn.isGone = true
                }
                val query = s.toString()
                if (isselected) {
                    callLookUpApi(query)
                }
                if (query.isNullOrEmpty()) {
                    binding.lookidRecyclerview.isGone = true
                }
            }

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                if (isselected) {
                    callLookUpApi(query)
                }

            }

        })

    }

    private fun callLookUpApi(query: String) {
        addDetailResumeVM.getLookUp(Constants.interests, query, "", "6")

    }

    fun isCondtionMet(): Boolean {
        if (!updateList.isEmpty()) {
            return true
        } else {
            binding.interestTextInputLayout.error=("please add interest")
            return false
        }
    }

    private fun apiCall() {

        binding.interestEdittext.setText("")
//        for (i in 0 until updateList.size) {
//            interests[0] = "$startWord + ${Helper.removeOneUnderscores(updateList[i])}"
//        }
        addDetailResumeVM.editInterest(
            InterestRequestModel(updateList)
        )
    }


}