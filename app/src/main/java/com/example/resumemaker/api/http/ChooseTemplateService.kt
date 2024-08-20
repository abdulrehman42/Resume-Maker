package com.example.resumemaker.api.http

import com.example.resumemaker.models.api.ProfileModel
import com.example.resumemaker.models.request.CreateProfileModel
import com.example.resumemaker.models.request.ObjectiveModelRequest
import com.example.resumemaker.models.request.QualificationRequestModel
import com.example.resumemaker.utils.Constants
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ChooseTemplateService {

    @GET(Constants.TEMPLATE_API)
    fun getHomeCategory(
        @Query("type") type: String
    ): Call<JsonElement>

    @POST(Constants.CREATE_PROFILE_API)
    fun onCreateProfile(@Body createProfileModel: CreateProfileModel): Call<JsonElement>

    @PUT(Constants.EDIT_OBJECTIVE)
    fun editObjective(@Path("profileId") profileId: String,
                      @Body objective: ObjectiveModelRequest): Call<JsonElement>

    @PUT(Constants.EDIT_EDUCATION)
    fun editEducation(
        @Path("profileId") profileId: String,
        @Body qualification: QualificationRequestModel): Call<JsonElement>

    @GET(Constants.SAMPLES_API)
    fun getSamples( @Query("type") type: String): Call<JsonElement>


}