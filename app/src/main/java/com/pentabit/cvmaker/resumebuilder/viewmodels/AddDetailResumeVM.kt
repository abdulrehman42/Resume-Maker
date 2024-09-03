package com.pentabit.cvmaker.resumebuilder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.api.repository.AddDetailResumeRepository
import com.pentabit.cvmaker.resumebuilder.callbacks.OnLookUpResult
import com.pentabit.cvmaker.resumebuilder.models.LoaderModel
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse
import com.pentabit.cvmaker.resumebuilder.models.api.Profile
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse
import com.pentabit.cvmaker.resumebuilder.models.api.SampleResponseModel
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.AchievementResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.EducationResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ExperienceResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ProjectResponse
import com.pentabit.cvmaker.resumebuilder.models.api.adddetailresume.ReferrenceResponseModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.CreateProfileRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.InterestRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.LanguageRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SingleItemRequestModel
import com.pentabit.cvmaker.resumebuilder.models.request.addDetailResume.SkillRequestModel
import com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE_ID
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDetailResumeVM @Inject constructor(val addDetailResumeRepository: AddDetailResumeRepository) :
    ViewModel() {
    val loadingState = MutableLiveData<LoaderModel>()
    val data = MutableLiveData<String>()
    var looksupResponse = MutableLiveData<List<LookUpResponse>>()
    val dataResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val objectiveResponse = MutableLiveData<ProfileModelAddDetailResponse>()
    val informationResponse = MutableLiveData<Profile>()
    val projectResponse = MutableLiveData<List<ProjectResponse>>()
    val referenceResponse = MutableLiveData<List<ReferrenceResponseModel>>()
    val achievementResponse = MutableLiveData<List<AchievementResponse>>()
    val experienceResponse = MutableLiveData<List<ExperienceResponse>>()
    val educationResponse = MutableLiveData<List<EducationResponse>>()
    val skillResponse = MutableLiveData<List<String>>()
    val interestResponse = MutableLiveData<List<String>>()
    val languageResponse = MutableLiveData<List<String>>()

    var isInEditMode = false


    val getSamples = MutableLiveData<List<SampleResponseModel>>()

    private fun getProfileId(): String {
        return AppsKitSDKPreferencesManager.getInstance().getStringPreferences(PROFILE_ID)
    }

    fun createProfile(createProfileRequestModel: CreateProfileRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))
            try {
                addDetailResumeRepository.createProfile(
                    createProfileRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            informationResponse.postValue(data as Profile)
                            isInEditMode = true
                            loadingState.postValue(LoaderModel(false, message!!))
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))
                        }
                    })
            } catch (e: Exception) {

                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun updateProfile(createProfileRequestModel: CreateProfileRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))
            try {
                addDetailResumeRepository.updateProfile(getProfileId(),
                    createProfileRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(LoaderModel(false, message!!))

                            informationResponse.postValue(data as Profile)
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))
                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editObjective(objective: SingleItemRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editObjective(
                    getProfileId(), objective,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            objectiveResponse.postValue(data as ProfileModelAddDetailResponse)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage!!))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editQualification(qualification: List<ProfileModelAddDetailResponse.UserQualification>) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editEducation(
                    getProfileId(), qualification,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            educationResponse.postValue(data as List<EducationResponse>)
                            loadingState.postValue(LoaderModel(false, message!!))
                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun getSample(type: String) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.getsample(
                    type,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            getSamples.postValue(data as List<SampleResponseModel>)
                            loadingState.postValue(LoaderModel(false, ""))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editSkill(skill: SkillRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editSkill(
                    getProfileId(), skill,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            skillResponse.postValue(data as List<String>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editInterest(interest: InterestRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editInterest(
                    getProfileId(), interest,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            interestResponse.postValue(data as List<String>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editLanguage(language: LanguageRequestModel) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editLanguage(
                    getProfileId(), language,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            languageResponse.postValue(data as List<String>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editExperience(experience: ArrayList<ProfileModelAddDetailResponse.UserExperience>) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editExperience(
                    getProfileId(), experience,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            experienceResponse.postValue(data as List<ExperienceResponse>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editReference(referenceRequestModel: ArrayList<ProfileModelAddDetailResponse.UserReference>) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editReference(
                    getProfileId(), referenceRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            referenceResponse.postValue(data as List<ReferrenceResponseModel>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editAchievement(achievRequestModel: ArrayList<ProfileModelAddDetailResponse.UserAchievement>) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editAchievement(
                    getProfileId(), achievRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            achievementResponse.postValue(data as List<AchievementResponse>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun editProjects(projectRequestModel: ArrayList<ProfileModelAddDetailResponse.UserProject>) {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.editProjects(
                    getProfileId(), projectRequestModel,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            projectResponse.postValue(data as List<ProjectResponse>)
                            loadingState.postValue(LoaderModel(false, message!!))

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun getProfileDetail() {
        viewModelScope.launch {
            loadingState.postValue(LoaderModel(true, ""))

            try {
                addDetailResumeRepository.getProfileDetail(
                    getProfileId(),
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            loadingState.postValue(LoaderModel(false, ""))
                            dataResponse.postValue(data as ProfileModelAddDetailResponse)

                        }

                        override fun onFailure(errorMessage: String?) {
                            loadingState.postValue(LoaderModel(false, errorMessage.toString()))

                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

    fun getLookUp(
        key: String,
        text: String,
        page: Int,
        size: Int,
        callback: OnLookUpResult
    ) {
        viewModelScope.launch {
            try {
                addDetailResumeRepository.getLookups(key, text, page, size,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            looksupResponse.postValue(data as List<LookUpResponse>)
                            callback.onLookUpResult(data)
                        }

                        override fun onFailure(errorMessage: String?) {
                            // do nothing
                        }
                    })
            } catch (e: Exception) {
                loadingState.postValue(LoaderModel(false, e.message.toString()))
                e.printStackTrace()
            }
        }
    }

}