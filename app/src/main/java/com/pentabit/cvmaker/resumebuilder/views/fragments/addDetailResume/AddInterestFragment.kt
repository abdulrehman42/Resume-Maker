package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddInterestFragment : BaseFragment<FragmentAddInterestBinding>()
{
    lateinit var addDetailResumeVM : AddDetailResumeVM
    var interest=ArrayList<String>()
    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.interestResponse.observe(requireActivity()) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
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

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text=getString(R.string.add_interest)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        val data_list=sharePref.readProfileData()
        if (data_list!=null)
        {
            interest= data_list.userInterests as ArrayList<String>
        }
        val data = sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.DATA)
        if (data!=null)
        {
            binding.interestEdittext.setText(data)
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
            if (isConditionMet()) {
                interest.add("-1__"+binding.interestEdittext.text.toString())
                apiCall()
            }else{
                currentActivity().showToast(getString(R.string.field_missing_error))

            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            addDetailResumeVM.isHide.value=true
            currentActivity().onBackPressedDispatcher.onBackPressed()

        }


    }
    fun isConditionMet(): Boolean {
        return !binding.interestEdittext.text.toString().isNullOrEmpty()
    }

    private fun apiCall() {
        addDetailResumeVM.editInterest(
            sharePref.readString(com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID).toString(),
            InterestRequestModel(interest)
        )
    }


}