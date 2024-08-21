package com.example.resumemaker.api.repository

import androidx.lifecycle.MutableLiveData
import com.example.resumemaker.api.SinglePointOfResponse
import com.example.resumemaker.api.http.ChooseTemplateService
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.json.JSONKeys
import com.example.resumemaker.json.JSONManager
import com.example.resumemaker.models.api.ProfileListingModel
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(private val chooseTemplateService: ChooseTemplateService) {
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

}