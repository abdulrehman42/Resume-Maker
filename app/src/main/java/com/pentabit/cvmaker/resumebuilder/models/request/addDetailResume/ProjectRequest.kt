package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class ProjectRequest(
    val projects: List<Project>
)
data class Project(
    val description: String,
    val title: String
)