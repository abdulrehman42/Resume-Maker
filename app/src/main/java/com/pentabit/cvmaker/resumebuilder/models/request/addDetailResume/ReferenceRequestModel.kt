package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class ReferenceRequestModel(
    val company: String,
    val email: String,
    val name: String,
    val phone: String,
    val position: String
)