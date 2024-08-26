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
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    @Named("GsonService")private val chooseTemplateService: ChooseTemplateService
) {
    private val _resumeResponse = MutableLiveData<NetworkResult<JsonElement>>()

    fun getProfileList(
        callback: ResponseCallback
    ) {
        _resumeResponse.postValue(NetworkResult.Loading())
        chooseTemplateService.getProfiles().enqueue(
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
                            object : TypeToken<List<ProfileListingModel>>() {}.type
                        ) as List<ProfileListingModel>
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
        _resumeResponse.postValue(NetworkResult.Loading())
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

}