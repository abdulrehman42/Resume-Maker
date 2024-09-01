package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddLanguageBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLanguageFragment(
    val data: List<String>?,
    val language: String?,
    val isedit: Boolean,
    val position: Int
) :
    BaseFragment<FragmentAddLanguageBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var languagePredictiveSearchHandler: PredictiveSearchHandler
    private val languagesAdapter = UserSkillAdapter()
    var list = ArrayList<String>()

    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate


    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_language)
        populateDataIfRequired()
        handlePredictiveSearch()
        handleClicks()
    }

    override fun observeLiveData() {
        addDetailResumeVM.languageResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun handlePredictiveSearch() {
        languagePredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.languages,
            isAlreadyInDB = isedit,
            autoCompleteTextView = binding.languageEdittext,
            viewModel = addDetailResumeVM,
            enableBtn = binding.tickBtn
        )
    }

    private fun populateDataIfRequired() {
        if (data != null) {
            list = data as ArrayList<String>
        }
        language?.let {
            binding.languageEdittext.setText(Helper.removeOneUnderscores(language))
        }
        binding.recyclerviewLanguage.adapter = languagesAdapter
    }

    private fun handleClicks() {
        binding.tickBtn.setOnClickListener {
            val skill = binding.languageEdittext.text.toString().trim()
            if (skill.isNotEmpty()) {
                if (!isedit) {
                    list.add(languagePredictiveSearchHandler.getText())
                } else {
                    list[position] = languagePredictiveSearchHandler.getText()
                }
                binding.languageEdittext.setText("")
                languagesAdapter.submitList(list.toList())
            }
        }

        binding.savebtn.setOnClickListener {
            if (list.isNotEmpty()) {
                saveLanguages()
            } else {
                AppsKitSDKUtils.makeToast("please add language")
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressed()
        }
    }

    private fun saveLanguages() {
        addDetailResumeVM.editLanguage(
            LanguageRequestModel(list)
        )
    }

}
