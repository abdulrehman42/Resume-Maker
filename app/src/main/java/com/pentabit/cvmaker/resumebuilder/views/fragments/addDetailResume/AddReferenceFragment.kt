package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.utils.PredictiveSearchHandler
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.utils.Validations
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddReferenceFragment(
    val userReference: ProfileModelAddDetailResponse.UserReference?,
    val list: ArrayList<ProfileModelAddDetailResponse.UserReference>?,
    val isedit: Boolean,
    val position: Int
) : BaseFragment<FragmentAddReferenceBinding>() {
    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val screenId = ScreenIDs.ADD_REFERENCE
    private lateinit var companyPredictiveSearchAdapter: PredictiveSearchHandler
    private lateinit var titlePredictiveSearchAdapter: PredictiveSearchHandler
    var oldList = ArrayList<ProfileModelAddDetailResponse.UserReference>()
    val updateList = ArrayList<ReferenceRequest.Reference>()

    override val inflate: Inflate<FragmentAddReferenceBinding>
        get() = FragmentAddReferenceBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        binding.includeTool.textView.text = getString(R.string.add_referrence)
        populateDataIfRequired()
        handlePredictiveSearch()
        handleClicks()
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as AdBaseActivity).askAdOnFragment(screenId)
    }

    override fun observeLiveData() {
        addDetailResumeVM.referenceResponse.observe(this) {
            parentFragmentManager.setFragmentResult(Constants.REFRESH_DATA, Bundle.EMPTY)
            currentActivity().onBackPressed()
        }
    }

    private fun populateDataIfRequired() {
        list?.let {
            oldList = list
            for (i in 0 until oldList.size) {
                updateList.add(
                    ReferenceRequest.Reference(
                        "1__" + Helper.removeOneUnderscores(oldList[i].company),
                        Helper.removeOneUnderscores(oldList[i].email),
                        Helper.removeOneUnderscores(oldList[i].name),
                        oldList[i].phone,
                        "1__" + Helper.removeOneUnderscores(oldList[i].position),
                    )
                )
            }
        }
        userReference?.let {
            binding.referrencenameedit.setText(Helper.removeOneUnderscores(userReference.name))
            binding.companyName.setText(Helper.removeOneUnderscores(userReference.company))
            binding.jobedittext.setText(Helper.removeOneUnderscores(userReference.position))
            binding.phone.setText(Helper.removeOneUnderscores(userReference.phone))
            binding.emailedit.setText(Helper.removeOneUnderscores(userReference.email))
            binding.phone.setText(userReference.phone)
        }
    }

    private fun handlePredictiveSearch() {
        companyPredictiveSearchAdapter = PredictiveSearchHandler(
            key = Constants.company,
            isAlreadyInDB = isedit,
            autoCompleteTextView = binding.companyName,
            viewModel = addDetailResumeVM
        )
        titlePredictiveSearchAdapter = PredictiveSearchHandler(
            key = Constants.position,
            isAlreadyInDB = isedit,
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
            currentActivity().onBackPressed()
        }
    }


    private fun saveReference() {
        if (!isedit) {
            updateList.add(
                ReferenceRequest.Reference(
                    companyPredictiveSearchAdapter.getText(),
                    binding.emailedit.text.toString(),
                    binding.referrencenameedit.text.toString(),
                    binding.phone.text.toString(),
                    titlePredictiveSearchAdapter.getText()
                )
            )
        } else {
            updateList[position] = ReferenceRequest.Reference(
                companyPredictiveSearchAdapter.getText(),
                binding.emailedit.text.toString(),
                binding.referrencenameedit.text.toString(),
                binding.phone.text.toString(),
                titlePredictiveSearchAdapter.getText()
            )
        }


        val referenceRequest = ReferenceRequest(references = updateList)

        addDetailResumeVM.editReference(referenceRequest)
    }
}