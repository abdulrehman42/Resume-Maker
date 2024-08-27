package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddReferenceFragment(val userReference: ProfileModelAddDetailResponse.UserReference?) : BaseFragment<FragmentAddReferenceBinding>() {
    lateinit var addDetailResumeVM : AddDetailResumeVM

    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.referenceResponse.observe(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            if (it.loader)
            {
                binding.loader.isGone=false
            }else{
                binding.loader.isGone=true
            }
            if (!it.msg.isNullOrBlank())
            {
                currentActivity().showToast(it.msg)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text = getString(R.string.add_referrence)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        userReference?.let {
            binding.referrencenameedit.setText(userReference.name)
            binding.companyName.setText(userReference.company)
            binding.emailedit.setText(userReference.email)
            binding.phone.setText(userReference.phone)
        }
        onclick()
    }

    private fun onclick() {
        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                apiCall()
                MainScope().launch {
                    delay(2000)
                    addDetailResumeVM.isHide.value = true
                    currentActivity().onBackPressedDispatcher.onBackPressed()
                }
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }
        /*requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            addDetailResumeVM.isHide.value=true
        }*/
    }

    fun isConditionMet(): Boolean {
        return !binding.jobedittext.text.toString().isNullOrEmpty() &&
                !binding.referrencenameedit.text.toString().isNullOrEmpty()&&
                Helper.isValidEmail(binding.emailedit.text.toString()) &&
                !binding.phone.text.toString().isNullOrEmpty() &&
                !binding.companyName.text.toString().isNullOrEmpty()
    }

    private fun apiCall() {
        val reference = listOf(
            ReferenceRequest.Reference("1__"+binding.companyName.text.toString(),binding.emailedit.text.toString()
                ,binding.referrencenameedit.text.toString(),binding.phone.text.toString(),"1__"+binding.jobedittext.text.toString())
        )

        val referenceRequest = ReferenceRequest(references = reference)

        addDetailResumeVM.editReference(
             referenceRequest)
    }
}