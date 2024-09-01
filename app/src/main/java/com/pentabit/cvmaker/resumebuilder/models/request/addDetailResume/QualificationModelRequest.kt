package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse

data class QualificationModelRequest(
    val qualifications: List<ProfileModelAddDetailResponse.UserQualification>
)

data class Qualification(
    val degree: String,
    val endDate: String?,
    val institute: String,
    val qualificationType: String,
    val startDate: String
)
