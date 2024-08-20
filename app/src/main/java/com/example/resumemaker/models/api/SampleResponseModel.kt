package com.example.resumemaker.models.api

data class SampleResponseModel(
    val body: String,
    val createdAt: String,
    val id: Int,
    val title: String,
    val type: String,
    val updatedAt: String
)