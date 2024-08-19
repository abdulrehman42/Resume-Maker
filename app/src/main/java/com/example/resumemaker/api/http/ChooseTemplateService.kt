package com.example.resumemaker.api.http

import com.example.resumemaker.utils.Constants
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ChooseTemplateService {

    @GET(Constants.TEMPLATE_API)
     fun getHomeCategory(
        @Query("type") type: String
    ): Call<JsonElement>



}