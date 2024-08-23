package com.pentabit.cvmaker.resumebuilder.models.api

data class Profile(

    val address: String,
    val baseUrl: String,
    val createdAt: String,
    val dob: String,
    val email: String,
    val gender: String,
    val id: Int,
    val jobTitle: String,
    val name: String,
    val objective: String? = null,
    val path: String,
    val phone: String,
    val updatedAt: String,
    val userId: String
)
