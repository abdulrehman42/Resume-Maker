package com.pentabit.cvmaker.resumebuilder.api.repository

import androidx.lifecycle.MutableLiveData
import com.pentabit.cvmaker.resumebuilder.api.SinglePointOfResponse
import com.pentabit.cvmaker.resumebuilder.api.http.ChooseTemplateService
import com.pentabit.cvmaker.resumebuilder.api.http.NetworkResult
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.json.JSONKeys
import com.pentabit.cvmaker.resumebuilder.json.JSONManager
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.models.api.CoverLetterResponse
import com.pentabit.cvmaker.resumebuilder.models.api.FcmResponse
import com.pentabit.cvmaker.resumebuilder.models.api.LoginModel
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LoginRequestModel
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
        email: String, authProvider: String,
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onLogin(email, authProvider).enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.TOKEN,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }

            })
        )
    }

    fun onFcm(
        token: String,
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onFCM(token).enqueue(
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
                            object : TypeToken<FcmResponse>() {}.type
                        ) as FcmResponse
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }

            })
        )
    }
    fun onDeleteProfile(
        profileId: String,
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.deleteProfile(profileId).enqueue(
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
                            object : TypeToken<String>() {}.type
                        ) as String
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }

            })
        )
    }

    fun getRefreshToken(
        callback: ResponseCallback
    ) {
        _templateResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onRefreshToken().enqueue(
            SinglePointOfResponse(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                    callback.onSuccess(
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.MESSAGE,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String,
                        JSONManager.getInstance().getFormattedResponse(
                            JSONKeys.TOKEN,
                            response.body(),
                            object : TypeToken<String>() {}.type
                        ) as String
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
                        com.pentabit.cvmaker.resumebuilder.json.JSONManager.getInstance()
                            .getFormattedResponse(
                                com.pentabit.cvmaker.resumebuilder.json.JSONKeys.MESSAGE,
                                response.body(),
                                object : TypeToken<String>() {}.type
                            ) as String,
                        com.pentabit.cvmaker.resumebuilder.json.JSONManager.getInstance()
                            .getFormattedResponse(
                                com.pentabit.cvmaker.resumebuilder.json.JSONKeys.DATA,
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
                        com.pentabit.cvmaker.resumebuilder.json.JSONManager.getInstance()
                            .getFormattedResponse(
                                com.pentabit.cvmaker.resumebuilder.json.JSONKeys.MESSAGE,
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
