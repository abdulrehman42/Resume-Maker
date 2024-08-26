package com.pentabit.cvmaker.resumebuilder.models.api

data class Profile(

    val profile: Profile1,
    val token: String
)

data class Profile1(
    val id: Long,
    val userID: String,
    val name: String,
    val email: String,
    val jobTitle: String,
    val phone: String,
    val address: String,
    val dob: String,
    val gender: String,
    val objective: Any? = null,
    val path: String,
    val baseURL: String,
    val createdAt: String,
    val updatedAt: String
)
