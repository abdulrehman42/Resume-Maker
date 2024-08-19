package com.example.resumemaker.models.api

data class TemplateModel(
    val baseUrl: String,
    val id: Int,
    val path: String,
    val type: String,
    val contentType: Int
)