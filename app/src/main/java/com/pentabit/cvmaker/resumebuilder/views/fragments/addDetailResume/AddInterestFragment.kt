package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddInterestFragment(
    val userInterests: List<String>?,
    val interest_name: String?,
    val isedit: Boolean,
    val position: Int
) : BaseFragment<FragmentAddInterestBinding>()
{
    lateinit var addDetailResumeVM : AddDetailResumeVM
    var oldList = ArrayList<String>()
    val updateList = ArrayList<String>()

    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.interestResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        addDetailResumeVM.loadingState.observe(viewLifecycleOwner){
            AppsKitSDKUtils.setVisibility(it.loader, binding.loader)
            if (it.msg.isNotBlank()) {
                AppsKitSDKUtils.makeToast(it.msg)
            }
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(this)[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text=getString(R.string.add_interest)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        userInterests?.let {
            oldList= userInterests as ArrayList<String>
            for (i in 0 until oldList.size)
            {
                updateList.add(oldList[i])
            }
        }
        if (interest_name!=null)
        {
            binding.interestEdittext.setText(interest_name)
        }
        onclick()

    }

   /* private fun onAdapter() {
        addinterest= SuggestionAdapter(currentActivity(), Helper.getSuggestions())
        {
            binding.interestEdittext.setText(it)
        }

        binding.recyclerviewSuggestions.apply {
            layoutManager= GridLayoutManager(currentActivity(),3)
            adapter=addinterest
        }


    }*/

    private fun onclick() {
        binding.savebtn.setOnClickListener {
            if (isCondtionMet()) {
                if (!isedit)
                {
                    updateList.add("-1__"+binding.interestEdittext.text.toString())
                    apiCall()
                }else{
                    updateList[position]="-1__"+binding.interestEdittext.text.toString()
                }

            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        currentActivity().onBackPressedDispatcher.addCallback{
            currentActivity().supportFragmentManager.popBackStackImmediate()

        }

    }
    fun isCondtionMet(): Boolean {
        if (!binding.interestEdittext.text.toString().isNullOrEmpty())
        {
            return true
        }else{
            AppsKitSDKUtils.makeToast("please add interest")
            return false
        }
    }

    private fun apiCall() {
        addDetailResumeVM.editInterest(
            InterestRequestModel(updateList)
        )
    }


}