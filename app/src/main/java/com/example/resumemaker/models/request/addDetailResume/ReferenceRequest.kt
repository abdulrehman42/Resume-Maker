package com.example.resumemaker.models.request.addDetailResume

data class ReferenceRequest(
    val references: List<Reference>
)
{
    data class Reference(
        val company: String,
        val email: String,
        val name: String,
        val phone: String,
        val position: String
    )
}