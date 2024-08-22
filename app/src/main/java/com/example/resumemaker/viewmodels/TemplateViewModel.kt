package com.example.resumemaker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.api.repository.TemplatesRepository
import com.example.resumemaker.models.api.CoverLetterResponse
import com.example.resumemaker.models.api.LoginModel
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import com.example.resumemaker.models.request.addDetailResume.LoginRequestModel
import com.example.resumemaker.models.request.addDetailResume.LookUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(private val templatesRepository: TemplatesRepository) :
    ViewModel() {
    val dataMap = MutableLiveData<Map<String, List<TemplateModel>>>()
    var isHide = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val dataResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val coverLetterResponse = MutableLiveData<CoverLetterResponse>()
    val getSamples = MutableLiveData<List<SampleResponseModel>>()
    val getString = MutableLiveData<String>()
    fun loginRequest(loginRequestModel: LoginRequestModel) {
        viewModelScope.launch {
            try {
                templatesRepository.getLogin(
                    loginRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            getString.postValue(data as String)
                        }

                        override fun onFailure(errorMessage: String?) {

                        }

                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetchTemplates(type: String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                templatesRepository.getTemplates(
                    type,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(false)
                            dataMap.postValue(data as Map<String, List<TemplateModel>>)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)

                        }

                    })
            } catch (e: Exception) {
                loadingState.postValue(false)
                e.printStackTrace()
            }
        }
    }

    fun createCoverLetter(coverLetterRequestModel: CoverLetterRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                templatesRepository.createCoverLetter(
                    coverLetterRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            coverLetterResponse.postValue(data as CoverLetterResponse)
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

    fun getSample(type: String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                templatesRepository.getsample(
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

    fun getCLPreview(id: String, templateID: String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                templatesRepository.getCLPreview(
                    id, templateID,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            getString.postValue(data as String)
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

    fun getResumePreview(id: String, templateID: String) {
//        viewModelScope.launch {
        loadingState.postValue(true)
        try {
            templatesRepository.getResumePreview(
                id, templateID,
                object : ResponseCallback {
                    override fun onSuccess(message: String?, data: Any?) {
                        getString.postValue(data as String)
                        loadingState.postValue(false)
                    }

                    override fun onFailure(errorMessage: String?) {
                        loadingState.postValue(false)
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
//        }
    }


}