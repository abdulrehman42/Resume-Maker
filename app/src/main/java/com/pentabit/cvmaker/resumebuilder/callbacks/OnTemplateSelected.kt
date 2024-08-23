package com.pentabit.cvmaker.resumebuilder.callbacks

import com.pentabit.cvmaker.resumebuilder.models.api.TemplateModel


interface OnTemplateSelected {
    fun onTemplateSelected(model: TemplateModel)

}