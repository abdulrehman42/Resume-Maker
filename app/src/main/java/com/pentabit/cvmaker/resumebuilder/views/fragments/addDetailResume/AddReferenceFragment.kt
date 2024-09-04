package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Utils
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReferenceFragment(
    val list: List<ProfileModelAddDetailResponse.UserReference>,
    val position: Int?,
    val callback:OnReferenceUpdate
) : BaseFragment<FragmentAddReferenceBinding>() {
    lateinit var addDetailResumeVM: AddDetailResumeVM
    private val screenId = ScreenIDs.ADD_REFERENCE
    private lateinit var companyPredictiveSearchAdapter: PredictiveSearchHandler
    private lateinit var titlePredictiveSearchAdapter: PredictiveSearchHandler
    private var isCompanyInDB = false
    private var isTitleInDB = false

    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_referrence)
        addDetailResumeVM = ViewModelProvider(this)[AddDetailResumeVM::class.java]
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

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun observeLiveData() {
        addDetailResumeVM.referenceResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }

    private fun populateDataIfRequired() {
        if (position != null) {
            val model = list[position]
            isTitleInDB= model.position.startsWith("1_")
            isCompanyInDB = model.company.startsWith("1_")

            binding.companyName.setText(Helper.removeOneUnderscores(model.company))
            binding.jobedittext.setText(Helper.removeOneUnderscores(model.position))
            binding.emailedit.setText(model.email)
            binding.phone.setText(model.phone)
            binding.referrencenameedit.setText(model.name)
        }

    }

    private fun handlePredictiveSearch() {
        companyPredictiveSearchAdapter = PredictiveSearchHandler(
            key = Constants.company,
            isAlreadyInDB = isCompanyInDB,
            autoCompleteTextView = binding.companyName,
            viewModel = addDetailResumeVM
        )
        titlePredictiveSearchAdapter = PredictiveSearchHandler(
            key = Constants.position,
            isAlreadyInDB = isTitleInDB,
            autoCompleteTextView = binding.jobedittext,
            viewModel = addDetailResumeVM
        )
    }


    private fun handleClicks() {
        binding.savebtn.setOnClickListener {
            if (Validations.isConditionMetReference(binding)) {
                saveReference()
            }
        }

        binding.includeTool.backbtn.setOnClickListener {
            currentActivity().supportFragmentManager.popBackStackImmediate()
        }
    }


    private fun saveReference() {
        val newReference = ProfileModelAddDetailResponse.UserReference(
            name = binding.referrencenameedit.text.toString(),
            company = companyPredictiveSearchAdapter.getText(),
            position = titlePredictiveSearchAdapter.getText(),
            email = binding.emailedit.text.toString(),
            phone = binding.phone.text.toString()
        )

        val updatedList =
            ArrayList<ProfileModelAddDetailResponse.UserReference>(list)

        if (position != null) {
            updatedList[position] = newReference
        } else {
            updatedList.add(newReference)
        }
        callback.referenceUpdate(updatedList)
        currentActivity().onBackPressedDispatcher.onBackPressed()
    }



    interface  OnReferenceUpdate{
        fun referenceUpdate(listReference:List<ProfileModelAddDetailResponse.UserReference>)
    }
}