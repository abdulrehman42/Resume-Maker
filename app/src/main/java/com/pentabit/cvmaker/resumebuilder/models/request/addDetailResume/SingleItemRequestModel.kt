package com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume

data class SingleItemRequestModel(
    val objective:String
)
data class SkillRequestModel(
    val skills:List<String>
)
data class InterestRequestModel(
    val interest:List<String>
)
data class LanguageRequestModel(
    val languages:List<String>
)
