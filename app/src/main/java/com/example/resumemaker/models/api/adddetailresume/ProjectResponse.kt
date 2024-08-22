package com.example.resumemaker.models.api.adddetailresume

data class ProjectResponse(
    val createdAt: String,
    val description: String,
    val id: Int,
    val profileId: Int,
    val title: String,
    val updatedAt: String
)