package com.pentabit.cvmaker.resumebuilder.models.api

data class ProfileListingModel(
    val baseUrl: String,
    val id: Int,
    val jobTitle: String,
    val name: String,
    val path: String
)