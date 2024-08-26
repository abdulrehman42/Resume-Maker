package com.pentabit.cvmaker.resumebuilder.api.http

import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SingleItemRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.google.gson.JsonElement
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ChooseTemplateService {

    //login
    @FormUrlEncoded
    @POST(Constants.LOGIN_API)
    fun onLogin(@Field("email") email: String,@Field("oauthProvider") authProvider: String): Call<JsonElement>

    @POST(Constants.TOKEN_REFRESH)
    fun onRefreshToken(): Call<JsonElement>


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
        @Part image: MultipartBody.Part,
        @Part("gender") gender: RequestBody,
        @Part("jobTitle") job: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody
    ): Call<JsonElement>

    @PUT(Constants.UPDATE_PROFILE)
    fun onUpdateProfile(
        @Path("profileId") profileId: String,
        @Part("name") name: RequestBody,
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part image: MultipartBody.Part,
        @Part("gender") gender: RequestBody,
        @Part("jobTitle") job: RequestBody,
        @Part("dob") dob: RequestBody,
        @Part("address") address: RequestBody
    ): Call<JsonElement>


    @GET(Constants.GETPROFILES_API)
    fun getProfiles(): Call<JsonElement>

    @GET(com.pentabit.cvmaker.resumebuilder.utils.Constants.GET_PROFILE_DETAIL)
    fun getProfileDetail(
        @Path("profileId") profileId: String,
    ): Call<JsonElement>

    //coverletter
    @POST(com.pentabit.cvmaker.resumebuilder.utils.Constants.CREATE_COVERLETTER)
    fun onCreateCoverLetter(@Body coverLetterRequestModel: CoverLetterRequestModel): Call<JsonElement>

    //add detail cv
    @PUT(Constants.EDIT_OBJECTIVE)
    fun editObjective(
        @Path("profileId") profileId: String,
        @Body objective: SingleItemRequestModel
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_EDUCATION)
    fun editEducation(
        @Path("profileId") profileId: String,
        @Body qualifications: QualificationModelRequest
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_SKILL)
    fun editSkill(
        @Path("profileId") profileId: String,
        @Body skills: SkillRequestModel
    ): Call<JsonElement>

    @PUT(Constants.EDIT_INTERESTS)
    fun editInterest(
        @Path("profileId") profileId: String,
        @Body interests: InterestRequestModel
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_LANGUAGE)
    fun editLanguage(
        @Path("profileId") profileId: String,
        @Body languages: LanguageRequestModel
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_REFERENCE)
    fun editReference(
        @Path("profileId") profileId: String,
        @Body references: ReferenceRequest
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_ACHIEVEMENTS)
    fun editAchievment(
        @Path("profileId") profileId: String,
        @Body achievements: AchievementRequest
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_EXPERIENCE)
    fun editExperience(
        @Path("profileId") profileId: String,
        @Body experiences: ExperienceRequest
    ): Call<JsonElement>

    @PUT(com.pentabit.cvmaker.resumebuilder.utils.Constants.EDIT_PROJECTS)
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

    @POST(Constants.FCM_API)
    fun onFCM(
        @Query("token") profileId: String,
    ): Call<JsonElement>

    @DELETE(Constants.DELTER_PROFILE)
    fun deleteProfile(
        @Path("profileId") profileId: String,
    ): Call<JsonElement>
    @DELETE(Constants.DELETE_ACCOUNT)
    fun deleteMe(
    ): Call<JsonElement>


}