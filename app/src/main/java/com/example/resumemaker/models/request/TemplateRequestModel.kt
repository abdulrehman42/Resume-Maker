package com.example.resumemaker.models.request

data class TemplateRequestModel(val type: String,
                           val category: String,
                           val page: Int,
                           val size: Int)