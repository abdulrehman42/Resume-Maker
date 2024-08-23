package com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume

data class ReferrenceResponseModel(
    val company: String,
    val createdAt: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val position: String,
    val updatedAt: String
)