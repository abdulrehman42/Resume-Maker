package com.example.resumemaker.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.api.repository.AddDetailResumeRepository
import com.example.resumemaker.models.api.ProfileModel
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.CreateProfileModel
import com.example.resumemaker.models.request.ObjectiveModelRequest
import com.example.resumemaker.models.request.QualificationRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDetailResumeVM @Inject constructor(val addDetailResumeRepository: AddDetailResumeRepository):ViewModel() {
    val loadingState = MutableLiveData<Boolean>()

    var fragment=MutableLiveData<Fragment>()
    var isHide=MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<ProfileModel>()
    val getSamples=MutableLiveData<List<SampleResponseModel>>()

    fun createProfile(createProfileModel: CreateProfileModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.createProfile(
                    createProfileModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataResponse.postValue(data as ProfileModel)
                            loadingState.postValue(false)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun getSample(type:String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.getsample(
                    type,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            getSamples.postValue(data as List<SampleResponseModel>)
                            loadingState.postValue(false)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun editObjective(profileId:String, objective: ObjectiveModelRequest) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editObjective(
                    profileId,objective,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataResponse.postValue(data as ProfileModel)
                            loadingState.postValue(false)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun editQualification(profileId:String, qualification: QualificationRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editEducation(
                    profileId,qualification,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataResponse.postValue(data as ProfileModel)
                            loadingState.postValue(false)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}