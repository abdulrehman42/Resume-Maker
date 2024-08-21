package com.example.resumemaker.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.resumemaker.api.SinglePointOfResponse
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.json.JSONKeys
import com.example.resumemaker.json.JSONManager
import com.example.resumemaker.models.api.CoverLetterResponse
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TemplatesRepository @Inject constructor(private val chooseTemplateService: ChooseTemplateService) {

    private val _templateResponse = MutableLiveData<NetworkResult<JsonElement>>()
    private val _resumeResponse = MutableLiveData<NetworkResult<JsonElement>>()

     fun getTemplates(
        type: String,
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getHomeCategory(type).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.DATA,
                            response.body(),
                            object : TypeToken<Map<String,List<TemplateModel>>>() {}.type
                        ) as Map<String, List<TemplateModel>>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }

            })
        )
    }
    fun getCLPreview(
        id:String,templateId:String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getCoverLetterPreview(id,templateId).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.DATA,
                            response.body(),
                            object : TypeToken<ProfileModelAddDetailResponse>() {}.type
                        ) as ProfileModelAddDetailResponse
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }
    fun createCoverLetter(
        coverLetterRequestModel: CoverLetterRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onCreateCoverLetter(coverLetterRequestModel).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.DATA,
                            response.body(),
                            object : TypeToken<CoverLetterResponse>() {}.type
                        ) as CoverLetterResponse
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun getsample(
        type:String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getSamples(type).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.DATA,
                            response.body(),
                            object : TypeToken<List<SampleResponseModel>>() {}.type
                        ) as List<SampleResponseModel>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }
    fun getResumePreview(
        id:String,templateId:String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getResumePreview(id,templateId).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.DATA,
                            response.body(),
                            object : TypeToken<TemplateModel>() {}.type
                        ) as ProfileModelAddDetailResponse
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }
}