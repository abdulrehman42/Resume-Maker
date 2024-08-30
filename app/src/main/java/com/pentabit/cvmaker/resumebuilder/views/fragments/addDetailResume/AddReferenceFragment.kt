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
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.LooksAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReferenceFragment(
    val userReference: ProfileModelAddDetailResponse.UserReference?,
    val list: ArrayList<ProfileModelAddDetailResponse.UserReference>?,
    val isedit: Boolean,
    val position: Int
) : BaseFragment<FragmentAddReferenceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    var startWith = "1__"
    var isCompany = false
    var isProgrammaticallySettingText = false
    var isProgrammaticallySettingText1 = false
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserReference>()
    val updateList = ArrayList<ReferenceRequest.Reference>()
    private var looksAdapter = LooksAdapter()
    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.referenceResponse.observe(this) {
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
            if (it.isEmpty()) {
                binding.recyclerviewLookup.isGone = true
                binding.recyclerviewLookupcompany.isGone = true
            } else {
                if (isCompany) {
                    binding.recyclerviewLookupcompany.isGone = false
                } else {
                    binding.recyclerviewLookup.isGone = false
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_referrence)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        list?.let {
            oldList = list
            for (i in 0 until oldList.size) {
                updateList.add(
                    ReferenceRequest.Reference(
                        oldList[i].company,
                        oldList[i].email,
                        oldList[i].name,
                        oldList[i].phone,
                        oldList[i].position,
                    )
                )
            }
        }
        userReference?.let {
            binding.referrencenameedit.setText(Helper.removeOneUnderscores(userReference.name))
            binding.companyName.setText(Helper.removeOneUnderscores(userReference.company))
            binding.jobedittext.setText(Helper.removeOneUnderscores(userReference.position))
            binding.phone.setText(Helper.removeOneUnderscores(userReference.phone))
            binding.emailedit.setText(Helper.removeOneUnderscores(userReference.email))
            binding.phone.setText(Helper.removeOneUnderscores(userReference.phone))
        }

        if (isCompany) {
            binding.recyclerviewLookupcompany.adapter = looksAdapter
        } else {
            binding.recyclerviewLookup.adapter = looksAdapter
        }
        onclick()
    }

    private fun onclick() {
        binding.companyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText1) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        binding.recyclerviewLookupcompany.isGone = true
                    } else {
                        callLookUpApiCompany(query)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isProgrammaticallySettingText1 = false
            }
        })

        binding.jobedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!isProgrammaticallySettingText) {
                    val query = s.toString()
                    if (query.isEmpty()) {
                        binding.recyclerviewLookup.isGone = true
                    } else {
                        callLookUpApiPosition(query)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                isProgrammaticallySettingText = false
            }
        })

        looksAdapter.setOnItemClickCallback {
            startWith = "1__"
            if (isCompany) {
                isProgrammaticallySettingText1 = true
                binding.companyName.setText(Helper.removeOneUnderscores(it.text))
                binding.recyclerviewLookupcompany.isGone = true
            } else {
                isProgrammaticallySettingText = true
                binding.jobedittext.setText(Helper.removeOneUnderscores(it.text))
                binding.recyclerviewLookup.isGone = true
            }
        }

        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetReference(binding)) {
                apiCall()
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun callLookUpApiCompany(query: String) {
        isCompany = true
        addDetailResumeVM.getLookUp(Constants.company, query, "", "")
    }

    private fun callLookUpApiPosition(query: String?) {
        isCompany = false
        addDetailResumeVM.getLookUp(Constants.position, query ?: "", "", "")
    }


    private fun apiCall() {
        if (!isedit)
        {
            updateList.add(
                ReferenceRequest.Reference(
                    startWith + binding.companyName.text.toString(),
                    binding.emailedit.text.toString(),
                    binding.referrencenameedit.text.toString(),
                    binding.phone.text.toString(),
                    startWith + binding.jobedittext.text.toString()
                )
            )
        }else{
            updateList[position]=ReferenceRequest.Reference(startWith + binding.companyName.text.toString(),
                binding.emailedit.text.toString(),
                binding.referrencenameedit.text.toString(),
                binding.phone.text.toString(),
                startWith + binding.jobedittext.text.toString())
        }


        val referenceRequest = ReferenceRequest(references = updateList)

        addDetailResumeVM.editReference(referenceRequest)
    }
}