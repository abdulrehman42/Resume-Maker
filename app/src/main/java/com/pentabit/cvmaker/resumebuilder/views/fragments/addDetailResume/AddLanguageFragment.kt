package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLanguageFragment(val data: List<String>?,val language:String?) : BaseFragment<FragmentAddLanguageBinding>()
{
    lateinit var addDetailResumeVM : AddDetailResumeVM
    var list=ArrayList<String>()
    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.languageResponse.observe(viewLifecycleOwner) {
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
        addDetailResumeVM= ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
//        val data_list=sharePref.readProfileData()
        binding.includeTool.textView.text=getString(R.string.add_language)
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            placeholder = ""
        )
        if (data!=null)
        {
            list= data as ArrayList<String>
        }
        language?.let{
            binding.languageEdittext.setText(language)
        }
       // onAdapter()
        onclick()
    }

    private fun onAdapter() {
       /* suggestionAdapter= SuggestionAdapter(currentActivity(), list)
        {

        }
        binding.recyclerviewLanguage.apply {
            layoutManager= GridLayoutManager(currentActivity(),3)
            adapter=suggestionAdapter
        }*/


    }

    @SuppressLint("NotifyDataSetChanged")
    private fun onclick() {

        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
                list.add("-1__"+binding.languageEdittext.text.toString())

                apiCall()
            }else{
                currentActivity().showToast(getString(R.string.single_field_missing_error))
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
        return !binding.languageEdittext.text.toString().isNullOrEmpty()
    }
    private fun apiCall() {
        addDetailResumeVM.editLanguage(
            LanguageRequestModel(list)
        )
    }

}
