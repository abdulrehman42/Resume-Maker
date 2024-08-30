package com.pentabit.cvmaker.resumebuilder.utils

import android.util.Patterns
import android.widget.TextView
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddAchievementsFRagmentBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddExperienceBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddProjectBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInformationBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentProjectBinding
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils

object Validations {

    fun validateFields(
        binding: FragmentInformationBinding,
        gender: String,
        isEditProfile: Boolean,
        getImage: String
    ): Boolean {
        // Check if all required fields are not null or empty
        /*if (!isFieldNotNullOrEmpty(
                binding.nameedittext,
                binding.phoneedittext,
                binding.jobedittext,
                binding.dobEdit
            )
        ) {
            AppsKitSDKUtils.makeToast("Please fill in all the required fields")
            return false
        }*/
        if (!isEditProfile && getImage.isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("Please select an image.")
            return false
        }
        if (binding.nameedittext.toString().isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("Please your name.")
            return false
        }


        // Check if the email is valid
        if (binding.emailtext.toString().isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("Please enter a valid email address.")
            return false
        }

        if (binding.phoneedittext.text.toString().length > 16) {
            AppsKitSDKUtils.makeToast("enter phone no less than 16 digits")
            return false
        }
        if (binding.phoneedittext.text.toString().length <= 6) {
            AppsKitSDKUtils.makeToast("enter phone no more than 6 digits")
            return false
        }


        // Check if gender is selected
        if (gender.isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("Please select a gender.")
            return false
        }

        // Check if it's edit profile or if the image is selected

        return true
    }

    fun isConditionMetEducation(binding: FragmentAddEducationBinding): Boolean {
        val startDate = binding.startdateedittext.text.toString()
        val endDate = binding.enddateedittext.text.toString()
        val isEndDateRequired = binding.checkItscontinue.isChecked

        return when {
            binding.instituenameedittext.text.toString().isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Institution name is required.")
                false
            }

            binding.degreeName.text.toString().isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Degree name is required.")
                false
            }

            startDate.isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Start date is required.")
                false
            }

            !isEndDateRequired && endDate.isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("End date is required if the 'Continue' checkbox is unchecked.")
                false
            }

            startDate == endDate -> {
                AppsKitSDKUtils.makeToast("Start date and end date should not be the same.")
                false
            }
            !endDate.isNullOrEmpty() && startDate > endDate -> {
                AppsKitSDKUtils.makeToast("Start date should not be greater than End date.")
                false
            }

            else -> true
        }
    }

    fun isConditionMetExperience(binding: FragmentAddExperienceBinding): Boolean {
        val startDate = binding.startdateedittext.text.toString()
        val endDate = binding.enddateedittext.text.toString()
        val isEndDateRequired = binding.checkItscontinue.isChecked

        return when {
            binding.companyName.text.toString().isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Company name is required.")
                false
            }

            binding.jobName.text.toString().isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Job name is required.")
                false
            }

            binding.description.text.toString().trim().isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Job description is required.")
                false
            }

            startDate.isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("Start date is required.")
                false
            }

            startDate == endDate -> {
                AppsKitSDKUtils.makeToast("Start date and end date should not be the same.")
                false
            }

            !isEndDateRequired && endDate.isNullOrEmpty() -> {
                AppsKitSDKUtils.makeToast("End date is required if 'Continue' is unchecked.")
                false
            }

            !endDate.isNullOrEmpty() && startDate > endDate -> {
                AppsKitSDKUtils.makeToast("Start date should not be greater than End date.")
                false
            }

            else -> true
        }
    }

    fun isConditionMetReference(binding: FragmentAddReferenceBinding): Boolean {
        when {
            binding.jobedittext.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Job field cannot be empty.")
                return false
            }

            binding.referrencenameedit.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Reference name cannot be empty.")
                return false
            }

            !Helper.isValidEmail(binding.emailedit.text.toString()) -> {
                AppsKitSDKUtils.makeToast("Please enter a valid email address.")
                return false
            }

            binding.phone.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Phone number cannot be empty.")
                return false
            }

            binding.companyName.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Company name cannot be empty.")
                return false
            }

            binding.phone.text.toString().length > 16 -> {
                AppsKitSDKUtils.makeToast("Enter phone no less than 16 digits")
                return false
            }

            binding.phone.text.toString().length <= 6 -> {
                AppsKitSDKUtils.makeToast("Company name cannot be empty.")
                return false
            }

            else -> {
                AppsKitSDKUtils.makeToast("All conditions are met.")
                return true
            }
        }
    }

    fun isConditionMetProject(binding: FragmentAddProjectBinding): Boolean {
        when {
            binding.projectedittext.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Project field cannot be empty.")
                return false
            }

            binding.descriptionedittext.text.toString().isEmpty() -> {
                AppsKitSDKUtils.makeToast("Description field cannot be empty.")
                return false
            }

            else -> {
                return true
            }
        }
    }

    fun conditionAchievemnet(binding: FragmentAddAchievementsFRagmentBinding): Boolean {
        if (binding.descriptionedittext.text.toString().isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("please add description")
            return false
        } else if (binding.achieveedittext.text.toString().isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("please add achievement")
            return false
        } else if (binding.issueDateeedittext.text.isNullOrEmpty()) {
            AppsKitSDKUtils.makeToast("please add issue date")
            return false
        } else {
            return true
        }
    }

    private fun isFieldNotNullOrEmpty(vararg views: TextView): Boolean {
        for (v in views) {
            if (v.text.toString().trim().isEmpty()) {
                return false
            }
        }
        return true
    }

}