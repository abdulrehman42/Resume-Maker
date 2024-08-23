package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class AchievementRequest(
    val achievements: List<Achievement>
)

    data class Achievement(
        val description: String,
        val issueDate: String,
        val title: String
    )
