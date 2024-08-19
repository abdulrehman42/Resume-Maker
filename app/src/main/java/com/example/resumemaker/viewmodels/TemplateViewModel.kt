package com.example.resumemaker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.api.http.ResponseCallback
import com.example.resumemaker.api.repository.TemplatesRepository
import com.example.resumemaker.models.api.TemplateModel
import com.google.gson.JsonElement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel @Inject constructor(private val templatesRepository: TemplatesRepository) :
    ViewModel() {
    val dataMap = MutableLiveData<Map<String, List<TemplateModel>>>()

    fun fetchTemplates(type: String) {
        viewModelScope.launch {
            try {
                templatesRepository.getTemplates(
                    type,
                    object : ResponseCallback {
                        override fun onSuccess(message: String?, data: Any?) {
                            dataMap.postValue(data as Map<String, List<TemplateModel>>)
                        }

                        override fun onFailure(errorMessage: String?) {

                        }

                    })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}