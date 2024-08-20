package com.example.resumemaker.models.request

import kotlinx.serialization.SerialName


data class CreateProfileModel(
    val name: String,
    val email: String,
    val phone: String,
    val image: String,
    val gender: String,
    @SerialName("jobTitle")
    val jobTitle: String,
    val dob: String,
    val address: String
)