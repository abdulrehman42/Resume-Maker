package com.example.resumemaker.models.request.addDetailResume

data class ExperienceRequest(
    val experiences: List<Experience>
)
data class Experience(
    val company: String,
    val description: String,
    val employmentType: String,
    val endDate: String?,
    val startDate: String,
    val title: String
)