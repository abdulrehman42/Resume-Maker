package com.example.resumemaker.api.http

import com.example.resumemaker.models.request.addDetailResume.AchievRequestModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequestModel
import com.example.resumemaker.models.request.addDetailResume.InterestRequestModel
import com.example.resumemaker.models.request.addDetailResume.LanguageRequestModel
import com.example.resumemaker.models.request.addDetailResume.ProjectRequestModel
import com.example.resumemaker.models.request.addDetailResume.QualificationModelRequest
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.models.request.addDetailResume.ReferenceRequestModel
import com.example.resumemaker.models.request.addDetailResume.SkillRequestModel
import com.example.resumemaker.utils.Constants
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ChooseTemplateService {

    @GET(Constants.TEMPLATE_API)
    fun getHomeCategory(
        @Query("type") type: String
    ): Call<JsonElement>

    //profile
    @POST(Constants.CREATE_PROFILE_API)
    fun onCreateProfile(@Body createProfileRequestModel: CreateProfileRequestModel): Call<JsonElement>
    @POST(Constants.UPDATE_PROFILE)
    fun onUpdateProfile(
        @Path ("profileId") profileId: String,
        @Body createProfileRequestModel: CreateProfileRequestModel
    ): Call<JsonElement>


    @GET(Constants.GETPROFILES_API)
    fun getProfiles(): Call<JsonElement>

    @GET(Constants.GET_PROFILE_DETAIL)
    fun getProfileDetail(
        @Path("profileId") profileId: String,
    ): Call<JsonElement>

    //coverletter
    @POST(Constants.CREATE_COVERLETTER)
    fun onCreateCoverLetter(@Body coverLetterRequestModel: CoverLetterRequestModel): Call<JsonElement>

    //add detail cv
    @PUT(Constants.EDIT_OBJECTIVE)
    fun editObjective(
        @Path("profileId") profileId: String,
        @Body objective: SingleItemRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_EDUCATION)
    fun editEducation(
        @Path("profileId") profileId: String,
        @Body qualifications: QualificationModelRequest
    ): Call<JsonElement>

    @PUT(Constants.EDIT_SKILL)
    fun editSkill(
        @Path("profileId") profileId: String,
        @Body skills: SkillRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_INTERESTS)
    fun editInterest(
        @Path("profileId") profileId: String,
        @Body interests: InterestRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_LANGUAGE)
    fun editLanguage(
        @Path("profileId") profileId: String,
        @Body languages: LanguageRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_REFERENCE)
    fun editReference(
        @Path("profileId") profileId: String,
        @Body references: ReferenceRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_ACHIEVEMENTS)
    fun editAchievment(
        @Path("profileId") profileId: String,
        @Body achievements: AchievRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_EXPERIENCE)
    fun editExperience(
        @Path("profileId") profileId: String,
        @Body experiences: ExperienceRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_PROJECTS)
    fun editProject(
        @Path("profileId") profileId: String,
        @Body projects: ProjectRequestModel
    ): Call<JsonElement>

    @GET(Constants.SAMPLES_API)
    fun getSamples(@Query("type") type: String): Call<JsonElement>

    //previewCoverLetter
    @GET(Constants.PREVIEW_COVERLETTER_API)
    fun getCoverLetterPreview(
        @Path("id") id: String,
        @Path("templateId") templateId: String,
    ): Call<JsonElement>

    //PreviewResume
    @GET(Constants.PREVIEW_RESUME_API)
    fun getResumePreview(
        @Path("profileId") profileId: String,
        @Path("templateId") templateId: String,
    ): Call<JsonElement>

}