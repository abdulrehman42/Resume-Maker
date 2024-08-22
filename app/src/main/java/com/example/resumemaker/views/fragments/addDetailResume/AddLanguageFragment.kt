package com.example.resumemaker.views.fragments.addDetailResume

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseFragment
import com.example.resumemaker.base.Inflate
import com.example.resumemaker.databinding.FragmentAddLanguageBinding
import com.example.resumemaker.models.request.addDetailResume.LanguageRequestModel
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.AddDetailResumeVM
import com.example.resumemaker.views.adapter.SuggestionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLanguageFragment : BaseFragment<FragmentAddLanguageBinding>()
{
    lateinit var addDetailResumeVM :AddDetailResumeVM
    lateinit var suggestionAdapter: SuggestionAdapter
    var list=ArrayList<String>()
    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.singleResponse.observe(viewLifecycleOwner) {
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

    @SuppressLint("SetTextI18n")
    override fun init(savedInstanceState: Bundle?) {
        addDetailResumeVM= ViewModelProvider(currentActivity())[AddDetailResumeVM::class.java]
        val data_list=sharePref.readProfileData()
        binding.includeTool.textView.text=getString(R.string.add_language)
        if (data_list!=null)
        {
            list= data_list.userLanguages as ArrayList<String>
        }
        val data=sharePref.readString(Constants.DATA)
        if (data!=null)
        {
            binding.languageEdittext.setText(data)
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
        binding.languageEdittext.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val enteredText = binding.languageEdittext.text.toString()
                list.add("1__"+enteredText)
                suggestionAdapter.notifyDataSetChanged()
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.languageEdittext.windowToken, 0)

                true // Return true to indicate that the action has been handled
            } else {
                false
            }
        }
        binding.savebtn.setOnClickListener {
            if (isConditionMet()) {
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
            sharePref.readString(Constants.PROFILE_ID).toString(),
            LanguageRequestModel(list)
        )
    }

}
