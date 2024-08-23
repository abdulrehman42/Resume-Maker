package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume



data class CreateProfileRequestModel(
    val name: String,
    val email: String,
    val phone: String?,
    val image: String,
    val gender: String,
    val jobTitle: String,
    val dob: String,
    val address: String?
)