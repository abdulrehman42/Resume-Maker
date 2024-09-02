package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddLanguageBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AddLanguageFragment(
    val data: List<String>,
    val position: Int?,
    val callback: OnLanguageUpdate

) :
    BaseFragment<FragmentAddLanguageBinding>() {
    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val screenId = ScreenIDs.ADD_LANGUAGE
    private lateinit var languagePredictiveSearchHandler: PredictiveSearchHandler
    var isLanguage = false
    private val languagesAdapter = UserSkillAdapter()
    var list = ArrayList<String>()

    override val inflate: Inflate<FragmentAddLanguageBinding>
        get() = FragmentAddLanguageBinding::inflate


    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_language)
        populateDataIfRequired()
        handlePredictiveSearch()
        handleClicks()
    }

    override fun observeLiveData() {
    }

    private fun handlePredictiveSearch() {
        languagePredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.languages,
            isAlreadyInDB = isLanguage,
            autoCompleteTextView = binding.languageEdittext,
            viewModel = addDetailResumeVM,
            enableBtn = binding.tickBtn
        )
    }

    private fun populateDataIfRequired() {
        list = data as ArrayList<String>
        if (position != null) {
            val model = data[position]
            isLanguage = model.startsWith("1_")
            binding.languageEdittext.setText(Helper.removeOneUnderscores(model))

        }


        languagesAdapter.submitList(data)
        binding.recyclerviewLanguage.adapter = languagesAdapter
    }

    private fun handleClicks() {
        binding.tickBtn.setOnClickListener {
            val language = binding.languageEdittext.text.toString().trim()
            if (language.isNotEmpty()) {
                if (position != null) {
                    list[position] = languagePredictiveSearchHandler.getText()
                } else {
                    list.add(languagePredictiveSearchHandler.getText())
                }
                binding.languageEdittext.setText("")

                languagesAdapter.submitList(list.toList())
                binding.recyclerviewLanguage.apply {
                    layoutManager = GridLayoutManager(requireContext(), 3)
                    adapter = languagesAdapter
                }
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
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun saveLanguages() {
        callback.languageUpdate(list)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }

    interface OnLanguageUpdate {
        fun languageUpdate(list: List<String>)
    }

}
