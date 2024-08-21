package com.example.resumemaker.models.request.addDetailResume

data class ExperienceRequestModel(
    val company: String,
    val description: String,
    val employmentType: String,
    val endDate: String?,
    val startDate: String,
    val title: String)