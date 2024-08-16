package com.example.resumemaker.api.http

import com.example.resumemaker.models.response.TemplateResponseModel
import com.example.resumemaker.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ChooseTemplateService {

    @GET(Constants.TEMPLATE_API)
    suspend fun getHomeCategory(
        @Query("type") type: String,
        @Query("category") category: String,
        @Query("pageSize") pageSize: Int,
        @Query("pageNumber") pageNumber: Int
    ): Response<TemplateResponseModel>

}