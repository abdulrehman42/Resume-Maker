package com.pentabit.cvmaker.resumebuilder.models.api

data class ProfileModelAddDetailResponse(
    val address: String?,
    val baseUrl: String?,
    val dob: String?,
    val email: String?,
    val gender: String?,
    val id: Int,
    val jobTitle: String?,
    val name: String?,
    val objective: String?,
    val path: String?,
    val phone: String?,
    val userAchievement: List<UserAchievement>?,
    val userExperiences: List<UserExperience>?,
    val userInterests: List<String>?,
    val userLanguages: List<String>?,
    val userProjects: List<UserProject>?,
    val userQualifications: List<UserQualification>?,
    val userReferences: List<UserReference>?,
    val userSkills: List<String>?
) {
    data class UserAchievement(
        val createdAt: String,
        val description: String,
        val id: Int,
        val issueDate: String,
        val title: String,
        val updatedAt: String
    )

    data class UserExperience(
        val company: String,
        val createdAt: String,
        val description: String,
        val employmentType: String,
        val endDate: String,
        val id: Int,
        val startDate: String,
        val title: String,
        val updatedAt: String
    )

    data class UserProject(
        val createdAt: String,
        val description: String,
        val id: Int,
        val title: String,
        val updatedAt: String
    )

    data class UserQualification(
        val degree: String,
        val institute: String,
        val startDate: String,
        val endDate: String?,
        val qualificationType: String,

    )

    data class UserReference(
        val company: String,
        val createdAt: String,
        val email: String,
        val id: Int,
        val name: String,
        val phone: String,
        val position: String,
        val updatedAt: String
    )
}