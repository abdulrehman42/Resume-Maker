package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse

data class AchievementRequest(
    val achievements: ArrayList<ProfileModelAddDetailResponse.UserAchievement>
)

    data class Achievement(
        val description: String,
        val issueDate: String,
        val title: String
    )
