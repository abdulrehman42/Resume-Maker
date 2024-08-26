package com.pentabit.cvmaker.resumebuilder.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.api.repository.AddDetailResumeRepository
import com.pentabit.cvmaker.resumebuilder.models.api.Profile
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDetailResumeVM @Inject constructor(val addDetailResumeRepository: AddDetailResumeRepository):ViewModel() {
    val loadingState = MutableLiveData<Boolean>()

    var fragment=SingleLiveEvent<Fragment>()
    var isHide=SingleLiveEvent<Boolean>()
    var looksupResponse=MutableLiveData<List<LookUpResponse>>()
    val dataResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val informationResponse=MutableLiveData<Profile>()
    val projectResponse=MutableLiveData<List<ProjectResponse>>()
    val referenceResponse=MutableLiveData<List<ReferrenceResponseModel>>()
    val achievementResponse=MutableLiveData<List<AchievementResponse>>()
    val experienceResponse=MutableLiveData<List<ExperienceResponse>>()
    val educationResponse=MutableLiveData<List<EducationResponse>>()
    val skillResponse=MutableLiveData<List<String>>()
    val interestResponse=MutableLiveData<List<String>>()
    val languageResponse=MutableLiveData<List<String>>()



    val getSamples=MutableLiveData<List<SampleResponseModel>>()

    fun createProfile(createProfileRequestModel: CreateProfileRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.createProfile(
                    createProfileRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            informationResponse.postValue(data as Profile)
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
    fun updateProfile(profile_id:String,createProfileRequestModel: CreateProfileRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.updateProfile(profile_id,
                    createProfileRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            informationResponse.postValue(data as Profile)
                            loadingState.postValue(false)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(false)
                        }
                    })
            } catch (e: Exception) {
                e.printStackTrace()
                loadingState.postValue(false)
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
                            dataResponse.postValue(data as ProfileModelAddDetailResponse)
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
    fun editQualification(profileId:String, qualification: QualificationModelRequest) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editEducation(
                    profileId,qualification,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            educationResponse.postValue(data as List<EducationResponse>)
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
    fun editSkill(profileId:String, skill: SkillRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editSkill(
                    profileId,skill,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            skillResponse.postValue(data as List<String>)
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
    fun editInterest(profileId:String, interest: InterestRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editInterest(
                    profileId, interest,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            interestResponse.postValue(data as List<String>)
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
        fun editLanguage(profileId: String, language: LanguageRequestModel) {
            viewModelScope.launch {
                loadingState.postValue(true)
                try {
                    addDetailResumeRepository.editLanguage(
                        profileId, language,
                        object : ResponseCallback {
                            override fun onSuccess(message: String?, data: Any?) {
                                languageResponse.postValue(data as List<String>)
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

    fun editExperience(profileId:String, experience: ExperienceRequest) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editExperience(
                    profileId, experience,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            experienceResponse.postValue(data as List<ExperienceResponse>)
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
        fun editReference(profileId:String, referenceRequestModel: ReferenceRequest) {
            viewModelScope.launch {
                loadingState.postValue(true)
                try {
                    addDetailResumeRepository.editReference(
                        profileId,referenceRequestModel,
                        object : ResponseCallback {
                            override fun onSuccess(message: String?, data: Any?) {
                                referenceResponse.postValue(data as List<ReferrenceResponseModel>)
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
    fun editAchievement(profileId:String, achievRequestModel: AchievementRequest) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editAchievement(
                    profileId, achievRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            achievementResponse.postValue(data as List<AchievementResponse>)
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

    fun editProjects(profileId:String, projectRequestModel: ProjectRequest) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.editProjects(
                    profileId, projectRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            projectResponse.postValue(data as List<ProjectResponse>)
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
    fun getProfileDetail(profileId:String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.getProfileDetail(
                    profileId,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataResponse.postValue(data as ProfileModelAddDetailResponse)
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
    fun getLookUp(key: String, query: String, s: String, s1: String) {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                addDetailResumeRepository.getLookups(
                    key,
                    query,s,s1,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            looksupResponse.postValue(data as List<LookUpResponse>)
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