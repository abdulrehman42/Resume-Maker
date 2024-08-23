package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class LoginRequestModel(
    val email: String,
    val oauthId: String,
    val oauthProvider: String,
    val phoneNumber: String
)