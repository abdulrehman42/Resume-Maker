package com.pentabit.cvmaker.resumebuilder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.api.repository.TemplatesRepository
import com.pentabit.cvmaker.resumebuilder.models.api.CoverLetterResponse
import com.pentabit.cvmaker.resumebuilder.models.api.FcmResponse
import com.pentabit.cvmaker.resumebuilder.models.api.LoginModel
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CoverLetterRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LoginRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants.AUTH_TOKEN
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(private val templatesRepository: TemplatesRepository) :
    ViewModel() {
    val dataMap = MutableLiveData<Map<String, List<TemplateModel>>>()
    var isHide = MutableLiveData<Boolean>()
    val loadingState = MutableLiveData<Boolean>()
    val loginResponse = MutableLiveData<String>()
    val dataResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val coverLetterResponse = MutableLiveData<CoverLetterResponse>()
    val getSamples = MutableLiveData<List<SampleResponseModel>>()
    val getString = MutableLiveData<String>()
    val fcmToken = MutableLiveData<FcmResponse>()


    fun loginRequest(email: String, authProvider: String) {
        loadingState.postValue(true)
        viewModelScope.launch {
            try {
                templatesRepository.getLogin(
                    email,authProvider,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(false)
                            AppsKitSDKPreferencesManager.getInstance()
                                .addInPreferences(AUTH_TOKEN, data as String)
                            loginResponse.postValue(data as String)
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



    fun onFCM(token: String) {
        loadingState.postValue(true)
        viewModelScope.launch {
            try {
                templatesRepository.onFcm(
                    token,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(false)
                            fcmToken.postValue(data as FcmResponse)
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

    fun onDeleteMe() {
        loadingState.postValue(true)
        viewModelScope.launch {
            try {
                templatesRepository.onDeleteMe(
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(false)
                            getString.postValue(data as String)
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



    fun tokenRefresh() {
        loadingState.postValue(true)
        viewModelScope.launch {
            try {
                templatesRepository.getRefreshToken(
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(false)
                            AppsKitSDKPreferencesManager.getInstance()
                                .addInPreferences(AUTH_TOKEN, data as String)
                            loginResponse.postValue(data as String)
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