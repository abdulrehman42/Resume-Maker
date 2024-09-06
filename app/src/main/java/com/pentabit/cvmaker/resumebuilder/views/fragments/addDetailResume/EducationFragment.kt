package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.callbacks.OnEducationUpdate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentEducationBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.EducationAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EducationFragment : AddDetailsBaseFragment<FragmentEducationBinding>(), OnEducationUpdate {

    private val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    private val educationAdapter = EducationAdapter()
    private var list = ArrayList<ProfileModelAddDetailResponse.UserQualification>()

    override val inflate: Inflate<FragmentEducationBinding>
        get() = FragmentEducationBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
        fetchProfileInfo()
    }

    private fun handleAdapter() {
        educationAdapter.disableEditing(false)

        educationAdapter.onDelete {
            if (list.size != 1) {
                deleteItemPopup(currentActivity(), "Do you want to delete this education record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    educationAdapter.submitList(list)
                                    educationAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            } else {
                AppsKitSDKUtils.makeToast("You need at least one education to proceed")
            }

        }

        educationAdapter.setOnClick { _, position ->
            (requireActivity() as AddDetailResume).openFragment(
                AddEducation(
                    list, position, this
                )
            )
        }

        binding.recyclerviewEducation.adapter = educationAdapter
    }

    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userQualifications.isNullOrEmpty(), binding.popup)
            if (!it.userQualifications.isNullOrEmpty())
                populateAdapter(it.userQualifications as ArrayList<ProfileModelAddDetailResponse.UserQualification>)
        }
    }

    private fun fetchProfileInfo() {
        addDetailResumeVM.getProfileDetail()
    }

    private fun handleClicks() {
        binding.addeducationbtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(AddEducation(list, null, this))
        }
    }

    private fun populateAdapter(qualificationList: List<ProfileModelAddDetailResponse.UserQualification>) {
        saveInListWithRequiredFormat(qualificationList)
        educationAdapter.submitList(list)
    }

    private fun saveInListWithRequiredFormat(qualificationList: List<ProfileModelAddDetailResponse.UserQualification>) {
        list.clear()
        for (qualification in qualificationList) {
            val model = ProfileModelAddDetailResponse.UserQualification(
                degree = "1__" + Helper.removeOneUnderscores(qualification.degree),
                institute = "1__" + Helper.removeOneUnderscores(qualification.institute),
                startDate = qualification.startDate,
                endDate = qualification.endDate,
                qualificationType = qualification.qualificationType
            )
            list.add(model)
        }
    }

    private fun saveEducationData() {
        addDetailResumeVM.editQualification(
            list
        )
    }

    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty()) {
            AppsKitSDKUtils.makeToast("Please add at least one education")
            return false

        } else {
            saveEducationData()
            return false
        }
    }

    override fun onEducationUpdated(userQualificationsList: MutableList<ProfileModelAddDetailResponse.UserQualification>) {
        list = ArrayList(userQualificationsList)
        AppsKitSDKUtils.setVisibility(userQualificationsList.isEmpty(), binding.popup)
        educationAdapter.submitList(userQualificationsList)
    }

}