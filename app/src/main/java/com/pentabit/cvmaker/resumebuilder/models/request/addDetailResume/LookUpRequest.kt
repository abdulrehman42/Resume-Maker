package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class LookUpRequest(
    val key:String,
    val text:String,
    val page:String?,
    val size:String?
)
