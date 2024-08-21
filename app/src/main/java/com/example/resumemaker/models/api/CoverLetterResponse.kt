package com.example.resumemaker.models.api

data class CoverLetterResponse(
    val body: String,
    val createdAt: String,
    val id: Int,
    val title: String,
    val updatedAt: String,
    val userId: String)