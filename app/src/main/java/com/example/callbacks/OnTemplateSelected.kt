package com.example.callbacks

import com.example.resumemaker.models.api.TemplateModel

interface OnTemplateSelected {
    fun onTemplateSelected(model: TemplateModel)
}