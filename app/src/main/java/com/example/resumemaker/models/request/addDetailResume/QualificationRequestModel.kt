package com.example.resumemaker.models.request.addDetailResume

data class QualificationRequestModel(
    val degree: String,
    val endDate: String?,
    val institute: String,
    val qualificationType: String,
    val startDate: String
)