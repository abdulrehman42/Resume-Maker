package com.example.resumemaker.api.http

import com.example.resumemaker.models.request.addDetailResume.AchievementRequest
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequest
import com.example.resumemaker.models.request.addDetailResume.InterestRequestModel
import com.example.resumemaker.models.request.addDetailResume.LanguageRequestModel
import com.example.resumemaker.models.request.addDetailResume.LoginRequestModel
import com.example.resumemaker.models.request.addDetailResume.ProjectRequest
import com.example.resumemaker.models.request.addDetailResume.QualificationModelRequest
import com.example.resumemaker.models.request.addDetailResume.ReferenceRequest
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.models.request.addDetailResume.SkillRequestModel
import com.example.resumemaker.utils.Constants
import com.google.gson.JsonElement
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChooseTemplateService {

    //login
    @POST(Constants.LOGIN_API)
    fun onLogin(@Body loginRequestModel: LoginRequestModel): Call<JsonElement>


    @GET(Constants.TEMPLATE_API)
    fun getHomeCategory(
        @Query("type") type: String
    ): Call<JsonElement>

    //profile

    /* @POST(Constants.CREATE_PROFILE_API)
     fun onCreateProfile(@Body createProfileRequestModel: CreateProfileRequestModel): Call<JsonElement>
 */
    @Multipart
    @POST(Constants.CREATE_PROFILE_API)
    fun createProfileWithImage(
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part?,
        @Part("gender") gender: RequestBody,
        @Part("job") job: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody
    ): Call<JsonElement>

    @POST(Constants.UPDATE_PROFILE)
    fun onUpdateProfile(
        @Path("profileId") profileId: String,
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
        @Body references: ReferenceRequest
    ): Call<JsonElement>

    @PUT(Constants.EDIT_ACHIEVEMENTS)
    fun editAchievment(
        @Path("profileId") profileId: String,
        @Body achievements: AchievementRequest
    ): Call<JsonElement>

    @PUT(Constants.EDIT_EXPERIENCE)
    fun editExperience(
        @Path("profileId") profileId: String,
        @Body experiences: ExperienceRequest
    ): Call<JsonElement>

    @PUT(Constants.EDIT_PROJECTS)
    fun editProject(
        @Path("profileId") profileId: String,
        @Body projects: ProjectRequest
    ): Call<JsonElement>

    @GET(Constants.SAMPLES_API)
    fun getSamples(@Query("type") type: String): Call<JsonElement>

    //previewCoverLetter
    @GET(Constants.PREVIEW_COVERLETTER_API)
    fun getCoverLetterPreview(
        @Path("id") id: String,
        @Path("templateId") templateId: String,
    ): Call<String>

    //PreviewResume
    @GET(Constants.PREVIEW_RESUME_API)
    fun getResumePreview(
        @Path("profileId") profileId: String,
        @Path("templateId") templateId: String,
    ): Call<String>

    @GET(Constants.LOOKUP_API)
    fun onGetLookup(
        @Query("key") key: String,
        @Query("text") query: String,
        @Query("page") s: String,
        @Query("size") s1: String
    ): Call<JsonElement>

}