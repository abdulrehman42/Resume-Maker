package com.example.resumemaker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resumemaker.api.http.NetworkResult
import com.example.resumemaker.models.response.TemplateResponseModel
import com.example.resumemaker.api.repository.TemplatesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TemplateViewModel@Inject constructor(private val templatesRepository: TemplatesRepository): ViewModel() {

    val templateResponse: MutableLiveData<NetworkResult<TemplateResponseModel>>
        get() = templatesRepository.templateResponse
    fun fetchTemplates(type: String,category: String,pageSize: Int,pageNumber: Int) {
        viewModelScope.launch {
            try {
                templatesRepository.getTemplates(type, category, pageSize, pageNumber)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


}