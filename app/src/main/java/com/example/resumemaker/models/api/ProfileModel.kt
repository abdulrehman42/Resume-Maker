package com.example.resumemaker.models.api

data class ProfileModel(
    val address: String,
    val baseUrl: String,
    val dob: String,
    val email: String,
    val gender: String,
    val id: Int,
    val jobTitle: String,
    val name: String,
    val objective: String,
    val path: String,
    val phone: String,
    val userAchievement: List<Any>,
    val userExperiences: List<Any>,
    val userInterests: List<Any>,
    val userLanguages: List<Any>,
    val userProjects: List<Any>,
    val userQualifications: List<Any>,
    val userReferences: List<Any>,
    val userSkills: List<Any>
)