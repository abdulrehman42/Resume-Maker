package com.pentabit.cvmaker.resumebuilder.utils

import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.widget.EditText
import android.widget.TextView
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddAchievementsFRagmentBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddEducationBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddExperienceBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddProjectBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentAddReferenceBinding
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentInformationBinding
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils


object Validations {

    fun infoScreenValidations(
        binding: FragmentInformationBinding,
        gender: String,
        isEditProfile: Boolean,
        getImage: String
    ): Boolean {
        if (!isEditProfile && getImage.isEmpty()) {
            AppsKitSDKUtils.makeToast("Please select an image.")
            return false
        }
        if (binding.nameedittext.toString().isEmpty()) {
            binding.nameTextInputLayout.error = ("Name is required.")
            return false
        }

        if (binding.emailtext.toString().isEmpty()) {
            binding.textInputLayout3.error = ("Email is required")
            return false
        }


        if (binding.phoneedittext.text.toString().length <= 6) {
            binding.textInputLayout4.error = ("Enter a valid number")
            return false
        }


        // Check if gender is selected
        if (gender.isEmpty()) {
            AppsKitSDKUtils.makeToast("Please select a gender.")
            return false
        }

        return true
    }

    fun limitEditTextCharacters(editText: EditText, maxLength: Int) {
        val filters = arrayOfNulls<InputFilter>(1)
        filters[0] = LengthFilter(maxLength)
        editText.filters = filters
    }


    fun isConditionMetEducation(binding: FragmentAddEducationBinding): Boolean {
        val startDate = binding.startdateedittext.text.toString()
        val endDate = binding.enddateedittext.text.toString()
        val isEndDateRequired = binding.checkItscontinue.isChecked

        return when {
            binding.instituenameedittext.text.toString().isNullOrEmpty() -> {
                binding.instituateTextInputLayout.error = ("Institution name is required.")
                false
            }

            binding.degreeName.text.toString().isNullOrEmpty() -> {
                binding.degreeTextInputLayout2.error = ("Degree name is required.")
                false
            }

            startDate.isNullOrEmpty() -> {
                binding.startdateTextInputLayout2.error = ("Start date is required.")
                false
            }

            !isEndDateRequired && endDate.isNullOrEmpty() -> {
                binding.enddateTextInputLayout2.error =
                    ("End date is required if the 'Continue' checkbox is unchecked.")
                false
            }

            startDate == endDate -> {
                binding.startdateTextInputLayout2.error =
                    ("Start date and end date should not be the same.")
                false
            }

            !endDate.isNullOrEmpty() && startDate > endDate -> {
                binding.enddateTextInputLayout2.error =
                    ("Start date should not be greater than End date.")
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
                binding.companyNameTextInputLayout2.error = ("Company name is required.")
                false
            }

            binding.jobName.text.toString().isNullOrEmpty() -> {
                binding.jobtitleTextInputLayout.error = ("Job name is required.")
                false
            }

            binding.description.text.toString().trim().isNullOrEmpty() -> {
                binding.descriptionTextInputLayout2.error = ("Job description is required.")
                false
            }


            startDate.isNullOrEmpty() -> {
                binding.startdateTextInputLayout2.error = ("Start date is required.")
                false
            }

            startDate == endDate -> {
                binding.enddateTextInputLayout2.error =
                    ("Start date and end date should not be the same.")
                false
            }

            !isEndDateRequired && endDate.isNullOrEmpty() -> {
                binding.enddateTextInputLayout2.error =
                    ("End date is required if 'Continue' is unchecked.")
                false
            }

            !endDate.isNullOrEmpty() && startDate > endDate -> {
                binding.startdateTextInputLayout2.error =
                    ("Start date should not be greater than End date.")
                false
            }

            else -> true
        }
    }

    fun isConditionMetReference(binding: FragmentAddReferenceBinding): Boolean {
        when {
            binding.jobedittext.text.toString().isEmpty() -> {
                binding.textInputLayout2.error = ("Job field cannot be empty.")
                return false
            }

            binding.referrencenameedit.text.toString().isEmpty() -> {
                binding.nameTextInputLayout.error = ("Reference name cannot be empty.")
                return false
            }


            binding.emailedit.text.toString().isEmpty() -> {
                binding.textInputLayout3.error = ("Please enter a valid email address.")
                return false
            }

            binding.phone.text.toString().isEmpty() -> {
                binding.textInputLayout5.error = ("Phone number cannot be empty.")
                return false
            }

            binding.companyName.text.toString().isEmpty() -> {
                binding.textInputLayout5.error = ("Company name cannot be empty.")
                return false
            }

            binding.phone.text.toString().length > 16 -> {
                binding.companyName.error = ("enter phone number less than 16 digits")
                return false
            }

            binding.phone.text.toString().length <= 6 -> {
                binding.textInputLayout5.error = ("enter phone number more than 6 digits")
                return false
            }

            else -> {
                return true
            }
        }
    }

    fun isConditionMetProject(binding: FragmentAddProjectBinding): Boolean {
        when {
            binding.projectedittext.text.toString().isEmpty() -> {
                binding.projecttitleTextInputLayout.error = ("Project field cannot be empty.")
                return false
            }

            binding.descriptionedittext.text.toString().isEmpty() -> {
                binding.descriptionTextInputLayout2.error = ("Description field cannot be empty.")
                return false
            }

            else -> {
                return true
            }
        }
    }

    fun conditionAchievemnet(binding: FragmentAddAchievementsFRagmentBinding): Boolean {
        if (binding.descriptionedittext.text.toString().isNullOrEmpty()) {
            binding.descriptionTextInputLayout2.error = ("please add description")
            return false
        } else if (binding.achieveedittext.text.toString().isNullOrEmpty()) {
            binding.achievementtitleTextInputLayout.error = ("please add achievement")
            return false
        } else if (binding.issueDateeedittext.text.isNullOrEmpty()) {
            binding.companyNameTextInputLayout2.error = ("please add issue date")
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