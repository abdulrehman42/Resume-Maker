package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddInterestBinding
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddInterestFragment(
    val userInterests: List<String>?,
    val interest_name: String?,
    val isedit: Boolean,
    val position: Int
) : BaseFragment<FragmentAddInterestBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var addInterestPredictiveSearchHandler: PredictiveSearchHandler
    private val screenId = ScreenIDs.ADD_INTERESTS
    private val userlanguasAdapter = UserSkillAdapter()
    var oldList = ArrayList<String>()
    val updateList = ArrayList<String>()

    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun observeLiveData() {
        addDetailResumeVM.interestResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_interest)
        populateDataIfRequired()
        handlePredictiveSearch()
        handleClicks()
    }

    private fun populateDataIfRequired() {
        userInterests?.let {
            oldList = userInterests as ArrayList<String>
            for (i in 0 until oldList.size) {
                updateList.add("1__" + Helper.removeOneUnderscores(oldList[i]))
            }
        }
        if (interest_name != null) {
            binding.interestEdittext.setText(interest_name)
        }
        binding.recyclerviewSuggestions.adapter = userlanguasAdapter
    }


    private fun handleClicks() {
        binding.savebtn.setOnClickListener {
            if (isCondtionMet()) {
//                if (!isedit) {
//                    for (i in 0 until oldList.size) {
//                        updateList.add("1__" + oldList[i])
//                    }
//                } else {
//                    updateList[position] = addInterestPredictiveSearchHandler.getText()
//                }
                saveInterests()
            }
        }
        binding.tickBtn.setOnClickListener {
            val skill = addInterestPredictiveSearchHandler.getText()
            if (skill.isNotEmpty()) {
                if (!isedit) {
                    updateList.add(skill)
                } else {
                    updateList[position] = skill
                }
                binding.interestEdittext.setText("")
                userlanguasAdapter.submitList(updateList.toList())
            }
        }
        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().onBackPressed()
        }
        currentActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

    }

    private fun handlePredictiveSearch() {
        addInterestPredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.interests,
            isAlreadyInDB = isedit,
            autoCompleteTextView = binding.interestEdittext,
            viewModel = addDetailResumeVM,
            enableBtn = binding.tickBtn
        )
    }

    fun isCondtionMet(): Boolean {
        return if (updateList.isNotEmpty()) {
            true
        } else {
            binding.interestTextInputLayout.error = ("please add interest")
            false
        }
    }

    private fun saveInterests() {
        binding.interestEdittext.setText("")
        addDetailResumeVM.editInterest(
            InterestRequestModel(updateList)
        )
    }


}