package com.example.resumemaker.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.models.response.TemplateResponseModel
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplatesRepository@Inject constructor(private val chooseTemplateService: ChooseTemplateService) {

    private val _templateResponse = MutableLiveData<NetworkResult<TemplateResponseModel>>()
    val templateResponse: MutableLiveData<NetworkResult<TemplateResponseModel>>
        get() = _templateResponse

    suspend fun getTemplates(type: String,category: String,pageSize: Int,pageNumber: Int) {
        _templateResponse.postValue(NetworkResult.Loading())
        val response = chooseTemplateService.getHomeCategory(type, category, pageSize, pageNumber)
        if (response.isSuccessful) {
            _templateResponse.postValue(NetworkResult.Success(response.body()!!))
        } else {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _templateResponse.postValue(NetworkResult.Error(errorObj.getString("message")))
        }
    }
}