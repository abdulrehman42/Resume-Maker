package com.example.resumemaker.models


data class ProfileModelData(
    val userName:String,
    val profession:String,
    val detail:Detail
){
    data class Detail(
        val intro:String,
        val gender:String,
        val location:String,
        val obj:String,
        val skill:List<SuggestionModel>,
        val exp:List<ExperienceModel>,
        val edu:List<EducationModel>,
        val certi:List<EducationModel>
    )
}