package com.pentabit.cvmaker.resumebuilder.api.repository

import androidx.lifecycle.MutableLiveData
import com.pentabit.cvmaker.resumebuilder.api.SinglePointOfResponse
import com.pentabit.cvmaker.resumebuilder.api.http.ChooseTemplateService
import com.pentabit.cvmaker.resumebuilder.api.http.NetworkResult
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.json.JSONKeys
import com.pentabit.cvmaker.resumebuilder.json.JSONManager
import com.pentabit.cvmaker.resumebuilder.utils.Helper.prepareMultipartRequest
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.pentabit.cvmaker.resumebuilder.models.api.Profile
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.AchievementResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.EducationResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ExperienceResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ProjectResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ReferrenceResponseModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.AchievementRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ExperienceRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ProjectRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.QualificationModelRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.ReferenceRequest
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SingleItemRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.ImageCompressorHelper
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class AddDetailResumeRepository @Inject constructor(
    @Named("GsonService") private val chooseTemplateService: ChooseTemplateService
) {
    private val _resumeResponse = MutableLiveData<NetworkResult<JsonElement>>()

    fun createProfile(
        profileModel: CreateProfileRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        val multipartParts = prepareMultipartRequest(profileModel)
        ImageCompressorHelper().compressInBackground(
            File(profileModel.image)
        ) {
            chooseTemplateService.createProfileWithImage(
                multipartParts["name"] as RequestBody,
                multipartParts["email"] as RequestBody,
                multipartParts["phone"] as RequestBody,
                it,
                multipartParts["gender"] as RequestBody,
                multipartParts["jobTitle"] as RequestBody,
                multipartParts["dob"] as RequestBody,
                multipartParts["address"] as RequestBody
            ).enqueue(
                SinglePointOfResponse(object : Callback<JsonElement> {
                    override fun onResponse(
                        call: Call<JsonElement>,
                        response: Response<JsonElement>
                    ) {
                        callback.onSuccess(
                            JSONManager.getInstance().getFormattedResponse(
                                JSONKeys.MESSAGE,
                                response.body(),
                                object : TypeToken<String>() {}.type
                            ) as String,
                            JSONManager.getInstance().getFormattedResponse(
                                JSONKeys.DATA,
                                response.body(),
                                object : TypeToken<Profile>() {}.type
                            ) as Profile
                        )
                    }

                    override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                        callback.onFailure(t.message)
                    }
                })
            )
        }

    }

    fun updateProfile(
        profile_id: String,
        profileModel: CreateProfileRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onUpdateProfile(profile_id, profileModel).enqueue(
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
        profileId: String,
        objective: SingleItemRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editObjective(profileId, objective).enqueue(
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
        profileId: String,
        qualification: QualificationModelRequest,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editEducation(profileId, qualification).enqueue(
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
                                object : TypeToken<List<EducationResponse>>() {}.type
                            ) as List<EducationResponse>
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
                       JSONManager.getInstance()
                            .getFormattedResponse(
                                JSONKeys.MESSAGE,
                                response.body(),
                                object : TypeToken<String>() {}.type
                            ) as String,
                        JSONManager.getInstance()
                            .getFormattedResponse(
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
        profileId: String,
        skill: SkillRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editSkill(profileId, skill).enqueue(
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
                                object : TypeToken<List<String>>() {}.type
                            ) as List<String>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editInterest(
        profileId: String,
        interest: InterestRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editInterest(profileId, interest).enqueue(
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
                                object : TypeToken<List<String>>() {}.type
                            ) as List<String>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editLanguage(
        profileId: String,
        language: LanguageRequestModel,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editLanguage(profileId, language).enqueue(
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
                                object : TypeToken<List<String>>() {}.type
                            ) as List<String>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editReference(
        profileId: String,
        referenceRequestModel: ReferenceRequest,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editReference(profileId, referenceRequestModel).enqueue(
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
                                object : TypeToken<List<ReferrenceResponseModel>>() {}.type
                            ) as List<ReferrenceResponseModel>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editAchievement(
        profileId: String,
        achievRequestModel: AchievementRequest,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editAchievment(profileId, achievRequestModel).enqueue(
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
                                object : TypeToken<List<AchievementResponse>>() {}.type
                            ) as List<AchievementResponse>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editExperience(
        profileId: String,
        experienceRequestModel: ExperienceRequest,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editExperience(profileId, experienceRequestModel).enqueue(
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
                            object : TypeToken<List<ExperienceResponse>>() {}.type
                        ) as List<ExperienceResponse>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun editProjects(
        profileId: String,
        projectRequestModel: ProjectRequest,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.editProject(profileId, projectRequestModel).enqueue(
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
                                object : TypeToken<List<ProjectResponse>>() {}.type
                            ) as List<ProjectResponse>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }

    fun getProfileDetail(
        profileId: String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getProfileDetail(profileId).enqueue(
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

    fun getLookups(
        key: String,
        query: String,
        s: String,
        s1: String,
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.onGetLookup(key, query, s, s1).enqueue(
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
                                object : TypeToken<List<LookUpResponse>>() {}.type
                            ) as List<LookUpResponse>
                    )
                }

                override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                    callback.onFailure(t.message)
                }
            })
        )
    }
}
