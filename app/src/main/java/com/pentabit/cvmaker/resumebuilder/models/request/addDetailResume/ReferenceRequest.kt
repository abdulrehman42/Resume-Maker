package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse

data class ReferenceRequest(
    val references: ArrayList<ProfileModelAddDetailResponse.UserReference>
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