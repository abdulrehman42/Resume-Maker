package com.example.resumemaker.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.resumemaker.api.SinglePointOfResponse
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.json.JSONKeys
import com.example.resumemaker.json.JSONManager
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.models.request.addDetailResume.AchievRequestModel
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequestModel
import com.example.resumemaker.models.request.addDetailResume.ProjectRequestModel
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.models.request.addDetailResume.QualificationRequestModel
import com.example.resumemaker.models.request.addDetailResume.ReferenceRequestModel
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddDetailResumeRepository @Inject constructor(private val chooseTemplateService: ChooseTemplateService) {
    private val _resumeResponse = MutableLiveData<NetworkResult<JsonElement>>()

    fun createProfile(
        profileModel: CreateProfileRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onCreateProfile(profileModel).enqueue(
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
    fun updateProfile(
        profile_id: String,
        profileModel: CreateProfileRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onUpdateProfile(profile_id,profileModel).enqueue(
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

    fun editObjective(
        profileId:String,
        objective: SingleItemRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editObjective(profileId,objective).enqueue(
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

    fun editEducation(
        profileId:String,
        qualification: QualificationRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editEducation(profileId,qualification).enqueue(
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

    fun editSkill(
        profileId:String,
        skill: SingleItemRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editSkill(profileId,skill).enqueue(
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
    fun editInterest(
        profileId:String,
        interest: SingleItemRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editInterest(profileId,interest).enqueue(
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
    fun editLanguage(
        profileId:String,
        language: SingleItemRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editLanguage(profileId,language).enqueue(
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
    fun editReference(
        profileId:String,
        referenceRequestModel: ReferenceRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editReference(profileId,referenceRequestModel).enqueue(
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
        fun editAchievement(
        profileId:String,
        achievRequestModel: AchievRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editAchievment(profileId,achievRequestModel).enqueue(
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

    fun editExperience(
        profileId:String,
        experienceRequestModel: ExperienceRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editExperience(profileId,experienceRequestModel).enqueue(
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

    fun editProjects(
        profileId:String,
        projectRequestModel: ProjectRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editProject(profileId,projectRequestModel).enqueue(
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
    fun getProfileDetail(
        profileId:String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getProfileDetail(profileId).enqueue(
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
}