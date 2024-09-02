package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse

data class ExperienceRequest(
    val experiences: ArrayList<ProfileModelAddDetailResponse.UserExperience>
)
data class Experience(
    val company: String,
    val description: String,
    val employmentType: String,
    val endDate: String?,
    val startDate: String,
    val title: String
)