package com.pentabit.cvmaker.resumebuilder.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pentabit.cvmaker.resumebuilder.api.http.ResponseCallback
import com.pentabit.cvmaker.resumebuilder.api.repository.ProfileRepository
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileListingModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class ProfileVM @Inject constructor(val profileRepository: ProfileRepository):
    ViewModel() {
    val loadingState = MutableLiveData<Boolean>()
    val dataResponse=MutableLiveData<List<ProfileListingModel>>()
    fun getProfileList() {
        viewModelScope.launch {
            loadingState.postValue(true)
            try {
                profileRepository.getProfileList(
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataResponse.postValue(data as List<ProfileListingModel>)
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