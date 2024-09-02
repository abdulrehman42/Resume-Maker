package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse

data class ProjectRequest(
    val projects: ArrayList<ProfileModelAddDetailResponse.UserProject>
)
data class Project(
    val description: String,
    val title: String
)