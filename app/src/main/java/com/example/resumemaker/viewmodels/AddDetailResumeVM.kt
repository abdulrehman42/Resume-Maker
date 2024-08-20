package com.example.resumemaker.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.api.repository.AddDetailResumeRepository
import com.example.resumemaker.models.api.ProfileModel
import com.example.resumemaker.models.api.SampleResponseModel
import com.example.resumemaker.models.request.addDetailResume.AchievRequestModel
import com.example.resumemaker.models.request.addDetailResume.CreateProfileRequestModel
import com.example.resumemaker.models.request.addDetailResume.ExperienceRequestModel
import com.example.resumemaker.models.request.addDetailResume.ProjectRequestModel
import com.example.resumemaker.models.request.addDetailResume.SingleItemRequestModel
import com.example.resumemaker.models.request.addDetailResume.QualificationRequestModel
import com.example.resumemaker.models.request.addDetailResume.ReferenceRequestModel
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

    fun createProfile(createProfileRequestModel: CreateProfileRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.createProfile(
                    createProfileRequestModel,
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
    fun editObjective(profileId:String, objective: SingleItemRequestModel) {
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

    fun editSkill(profileId:String, skill: SingleItemRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editSkill(
                    profileId,skill,
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
    fun editInterest(profileId:String, interest: SingleItemRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editInterest(
                    profileId, interest,
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
        fun editLanguage(profileId: String, language: SingleItemRequestModel) {
            viewModelScope.launch {
                loadingState.postValue(true)
                try {
                    addDetailResumeRepository.editLanguage(
                        profileId, language,
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

    fun editExperience(profileId:String, experience: ExperienceRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editExperience(
                    profileId, experience,
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
        fun editReference(profileId:String, referenceRequestModel: ReferenceRequestModel) {
            viewModelScope.launch {
                loadingState.postValue(true)
                try {
                    addDetailResumeRepository.editReference(
                        profileId,referenceRequestModel,
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
    fun editAchievement(profileId:String, achievRequestModel: AchievRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editAchievement(
                    profileId, achievRequestModel,
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

    fun editProjects(profileId:String, projectRequestModel: ProjectRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editProjects(
                    profileId, projectRequestModel,
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