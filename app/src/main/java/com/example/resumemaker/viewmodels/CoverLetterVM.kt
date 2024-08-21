package com.example.resumemaker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.api.repository.CoverLetterRepository
import com.example.resumemaker.models.api.CoverLetterResponse
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.addDetailResume.CoverLetterRequestModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CoverLetterVM @Inject constructor(val coverLetterRepository: CoverLetterRepository):
    ViewModel() {
    val loadingState = MutableLiveData<Boolean>()

    val dataResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val coverLetterResponse = MutableLiveData<CoverLetterResponse>()
    val getSamples= MutableLiveData<List<SampleResponseModel>>()
    val getString=MutableLiveData<String>()
    fun createCoverLetter(coverLetterRequestModel: CoverLetterRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                coverLetterRepository.createCoverLetter(
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
    fun getSample(type:String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                coverLetterRepository.getsample(
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
    fun getCLPreview(id:String,templateID:String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                coverLetterRepository.getCLPreview(
                    id,templateID,
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
    fun getResumePreview(id:String,templateID:String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                coverLetterRepository.getResumePreview(
                    id,templateID,
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
}