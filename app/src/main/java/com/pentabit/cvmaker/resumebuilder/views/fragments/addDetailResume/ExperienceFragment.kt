package com.pentabit.cvmaker.resumebuilder.views.fragments.addDetailResume

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import com.pentabit.cvmaker.resumebuilder.base.AddDetailsBaseFragment
import com.pentabit.cvmaker.resumebuilder.base.Inflate
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentExperienceBinding
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.Experience
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.deleteItemPopup
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.cvmaker.resumebuilder.views.activities.AddDetailResume
import com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume.ExperienceAdapter
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExperienceFragment : AddDetailsBaseFragment<FragmentExperienceBinding>(),
    AddExperienceFragment.OnExperienceUpdate {

    val addDetailResumeVM: AddDetailResumeVM by activityViewModels()
    val experienceAdapter = ExperienceAdapter()
    var list = ArrayList<ProfileModelAddDetailResponse.UserExperience>()

    override val inflate: Inflate<FragmentExperienceBinding>
        get() = FragmentExperienceBinding::inflate

    override fun init(savedInstanceState: Bundle?) {
        handleAdapter()
        handleClicks()
    }

    private fun handleClicks() {


        binding.addexperiencebtn.setOnClickListener {
            (requireActivity() as AddDetailResume).openFragment(
                AddExperienceFragment(
                    list,
                    null, this
                )
            )
        }

    }


    override fun observeLiveData() {
        addDetailResumeVM.dataResponse.observe(this) {
            AppsKitSDKUtils.setVisibility(it.userExperiences.isNullOrEmpty(), binding.popup)
            populateAdapter(it.userExperiences as ArrayList<ProfileModelAddDetailResponse.UserExperience>)
        }
    }


    private fun handleAdapter() {

        experienceAdapter.setOnEditItemClickCallback { item, position ->

            (requireActivity() as AddDetailResume).openFragment(
                AddExperienceFragment(
                    list,
                    position,
                    this
                )
            )

        }
        experienceAdapter.setOnItemDeleteClickCallback {
            if (list.size!=1){
                deleteItemPopup(currentActivity(), "Do you want to delete this Experience record",
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                if (list.isNotEmpty()) {
                                    list.removeAt(it)
                                    experienceAdapter.submitList(list)
                                    experienceAdapter.notifyDataSetChanged()
                                }
                            }
                        }
                    })
            }else{
                AppsKitSDKUtils.makeToast("sorry at least one experience required")

            }

        }
        binding.recyclerviewExperience.adapter = experienceAdapter

    }


    override fun csnMoveForward(): Boolean {
        return true
    }

    override fun onMoveNextClicked(): Boolean {
        if (list.isEmpty())
        {
           // AppsKitSDKUtils.makeToast("please Add at least one skill")
            return true

        }else{
            saveExperienceData()
            return false
        }
    }

    private fun apiCall() {
        addDetailResumeVM.getProfileDetail()
    }

    override fun onResume() {
        super.onResume()
        parentFragmentManager.setFragmentResultListener(Constants.REFRESH_DATA, this) { _, _ ->
            apiCall()
        }
    }


    private fun saveExperienceData() {
        addDetailResumeVM.editExperience(
            list
        )
    }

    private fun populateAdapter(qualificationList: List<ProfileModelAddDetailResponse.UserExperience>) {
        saveInListWithRequiredFormat(qualificationList)
        experienceAdapter.submitList(list)
    }

    private fun saveInListWithRequiredFormat(experienceList: List<ProfileModelAddDetailResponse.UserExperience>) {
        for (experience in experienceList) {
            val model = ProfileModelAddDetailResponse.UserExperience(
                company = "1__" + Helper.removeOneUnderscores(experience.company),
                description = experience.description,
                employmentType = "fullTime",
                startDate = experience.startDate,
                endDate = experience.endDate,
                title = "1__" + Helper.removeOneUnderscores(experience.title),
            )
            list.add(model)
        }
    }



    override fun onExperience(experiencelist: ArrayList<ProfileModelAddDetailResponse.UserExperience>?) {
        list = ArrayList(experiencelist)
        experiencelist?.let {
            AppsKitSDKUtils.setVisibility(experiencelist.isEmpty(), binding.popup)
        }
        experienceAdapter.submitList(experiencelist)
    }


}