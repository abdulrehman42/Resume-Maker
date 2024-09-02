package com.pentabit.cvmaker.resumebuilder.callbacks

import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse.UserAchievement
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse.UserProject
import com.pentabit.cvmaker.resumebuilder.models.api.ProfileModelAddDetailResponse.UserReference

interface OnSkillUpdate {
    fun onSkill(skillList: List<String?>?)
    fun onLanguage(skillList: List<String?>?)
    fun onInterest(skillList: List<String?>?)
    fun onReference(userReferenceList: List<UserReference?>?)
    fun onProject(userReferenceList: List<UserProject?>?)
    fun onAchievement(userReferenceList: List<UserAchievement?>?)

}