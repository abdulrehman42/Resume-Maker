package com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume

data class ExperienceResponse(
    val company: String,
    val createdAt: String,
    val description: String,
    val employmentType: String,
    val endDate: Any,
    val id: Int,
    val startDate: String,
    val title: String,
    val updatedAt: String
)