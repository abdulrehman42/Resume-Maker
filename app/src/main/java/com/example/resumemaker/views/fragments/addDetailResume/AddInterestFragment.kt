package com.example.resumemaker.views.fragments.addDetailResume

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddInterestBinding
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequestModel
import com.example.resumemaker.models.request.addDetailResume.InterestRequestModel
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddInterestFragment : BaseFragment<FragmentAddInterestBinding>()
{
    lateinit var addDetailResumeVM :AddDetailResumeVM
    var interest=ArrayList<String>()
    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.singleResponse.observe(requireActivity()) {
            addDetailResumeVM.isHide.value = true
            currentActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM=ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        binding.includeTool.textView.text=getString(R.string.add_interest)
        val data_list=sharePref.readProfileData()
        if (data_list!=null)
        {
            interest= data_list.userInterests as ArrayList<String>
        }
        val data = sharePref.readString(Constants.DATA)
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
                interest.add("1__"+binding.interestEdittext.text.toString())
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
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
//            addDetailResumeVM.isHide.value=true
//        }

    }
    fun isConditionMet(): Boolean {
        return !binding.interestEdittext.text.toString().isNullOrEmpty()
    }

    private fun apiCall() {
        addDetailResumeVM.editInterest(
            sharePref.readString(Constants.PROFILE_ID).toString(),
            InterestRequestModel(interest)
        )
    }


}