package com.example.resumemaker.callbacks

import com.example.resumemaker.models.api.ProfileModel
import com.example.resumemaker.models.api.TemplateModel

interface OnTemplateSelected {
    fun onTemplateSelected(model: TemplateModel)

}