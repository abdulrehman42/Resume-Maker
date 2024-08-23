package com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume

data class EducationResponse(
    val createdAt: String,
    val degree: String,
    val endDate: Any,
    val id: Int,
    val institute: String,
    val qualificationType: String,
    val startDate: String,
    val updatedAt: String
)