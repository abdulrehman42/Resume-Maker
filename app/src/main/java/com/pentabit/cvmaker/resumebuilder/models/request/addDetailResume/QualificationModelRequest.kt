package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class QualificationModelRequest(
    val qualifications: List<Qualification>
)
    data class Qualification(
        val degree: String,
        val endDate: String?,
        val institute: String,
        val qualificationType: String,
        val startDate: String
    )
