package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddInterestBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.UserSkillAdapter
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddInterestFragment(
    val userInterests: List<String>,
    val position: Int?,
    val callback: OnInterestUpdate
) : BaseFragment<FragmentAddInterestBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private lateinit var addInterestPredictiveSearchHandler: PredictiveSearchHandler
    var isInterestInDB = false
    private val screenId = ScreenIDs.ADD_INTERESTS
    private val userlanguasAdapter = UserSkillAdapter()
    var oldList = ArrayList<String>()
    val updateList = ArrayList<String>()

    override val inflate: Inflate<FragmentAddInterestBinding>
        get() = FragmentAddInterestBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_interest)
        populateDataIfRequired()
        handlePredictiveSearch()
        handleClicks()
        manageAds()
    }

    private fun manageAds() {
        AppsKitSDKAdsManager.showBanner(
            currentActivity(),
            binding.bannerAdd,
            Utils.createAdKeyFromScreenId(screenId)
        )
    }

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


    private fun populateDataIfRequired() {
        if (position != null) {
            val model = userInterests[position]
            binding.interestEdittext.setText(Helper.removeOneUnderscores(model))
            isInterestInDB = model.startsWith("1_")

        }
        userInterests?.let {
            oldList = userInterests as ArrayList<String>
            for (i in 0 until oldList.size) {
                updateList.add("1__" + Helper.removeOneUnderscores(oldList[i]))
            }
        }
        userlanguasAdapter.submitList(updateList.toList())

        binding.recyclerviewSuggestions.adapter = userlanguasAdapter
    }


    private fun handleClicks() {
        binding.savebtn.setOnClickListener {
            if (!updateList.isEmpty()) {
                saveInterests()
            } else {
                binding.interestTextInputLayout.error = ("please add skill")
            }
        }
        binding.tickBtn.setOnClickListener {
            val interest = addInterestPredictiveSearchHandler.getText()
            /* val isNumeric = interest.matches(Regex("\\d+"))
             if (!isNumeric) {*/
            if (interest.isNotEmpty()) {
                if (position != null) {
                    updateList[position] = addInterestPredictiveSearchHandler.getText()
                } else {
                    updateList.add(addInterestPredictiveSearchHandler.getText())
                }
                binding.interestEdittext.setText("")
                userlanguasAdapter.submitList(updateList.toList())
            }
            /*}else{
                AppsKitSDKUtils.makeToast("please add interest don't ")
            }*/
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
        currentActivity().onBackPressedDispatcher.addCallback {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }

    }

    private fun handlePredictiveSearch() {
        addInterestPredictiveSearchHandler = PredictiveSearchHandler(
            key = Constants.interests,
            isAlreadyInDB = isInterestInDB,
            autoCompleteTextView = binding.interestEdittext,
            viewModel = addDetailResumeVM,
            enableBtn = binding.tickBtn
        )
    }

    private fun saveInterests() {
        callback.onInterest(updateList)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }

    interface OnInterestUpdate {
        fun onInterest(interesList: List<String>)
    }

}