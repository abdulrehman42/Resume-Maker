package com.example.resumemaker.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.resumemaker.api.SinglePointOfResponse
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.json.JSONKeys
import com.example.resumemaker.json.JSONManager
import com.example.resumemaker.models.api.CoverLetterResponse
import com.example.resumemaker.models.api.LookUpResponse
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.models.request.addDetailResume.LoginRequestModel
import com.example.resumemaker.models.request.addDetailResume.LookUpRequest
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class TemplatesRepository @Inject constructor(
    @Named("GsonService") private val chooseTemplateService: ChooseTemplateService,
    @Named("ScalarsService") private val chooseTemplateServiceForHTMLd: ChooseTemplateService
) {

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
                            object : TypeToken<Map<String, List<TemplateModel>>>() {}.type
                        ) as Map<String, List<TemplateModel>>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }

            })
        )
    }

    fun getLogin(
        loginRequestModel: LoginRequestModel,
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onLogin(loginRequestModel).enqueue(
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
                            object : TypeToken<Map<String, List<TemplateModel>>>() {}.type
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
        id: String, templateId: String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateServiceForHTMLd.getCoverLetterPreview(id, templateId)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        try {
                            response.body()?.let { jsonElement ->
                                callback.onSuccess(
                                    "",
                                    jsonElement
                                )
                            } ?: callback.onFailure("Response body is null")
                        } catch (e: Exception) {
                            callback.onFailure("Failed to parse JSON: ${e.message}")
                        }
                    } else {
                        callback.onFailure("Response failed with status: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            }
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
        type: String,
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
        id: String, templateId: String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())

        chooseTemplateServiceForHTMLd.getResumePreview(id, templateId)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        try {
                            response.body()?.let { jsonElement ->
                                callback.onSuccess(
                                    "",
                                    jsonElement
                                )
                            } ?: callback.onFailure("Response body is null")
                        } catch (e: Exception) {
                            callback.onFailure("Failed to parse JSON: ${e.message}")
                        }
                    } else {
                        callback.onFailure("Response failed with status: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            }
            )
    }

}
