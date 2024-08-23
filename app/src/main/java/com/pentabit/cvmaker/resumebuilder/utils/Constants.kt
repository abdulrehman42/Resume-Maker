package com.pentabit.cvmaker.resumebuilder.utils

import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager

object Constants {
    const val BASE_MEDIA_URL = "https://n0ngl4zx.tinifycdn.com/"
    const val BASE_URL = "http://35.164.228.120:9000/v1/"
    const val GUEST_TOKEN = "guestToken"
    var TOKEN = AppsKitSDKPreferencesManager.getInstance().getStringPreferences(
        GUEST_TOKEN,
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6ImU2MmMyYjk0LTAyNGEtNDgwZi1iNjc3LWFmMjNjMTBjMGUxYSIsIm5hbWUiOm51bGwsImVtYWlsIjoicmVjb21tZW5kbWVAcGVudGFiaXRhcHBzLmNvbSIsIm9hdXRoSWQiOm51bGwsInBob25lTGFuZ3VhZ2UiOm51bGwsIm9hdXRoUHJvdmlkZXIiOiJlbWFpbCIsImlzRGVsZXRlZCI6ZmFsc2UsInByb2ZpbGVzIjpbXSwidXNlclR5cGUiOiJ1c2VyIiwiaWF0IjoxNzA2MjY5NjcyfQ.bq09dt2Cj6vP8lh1UKHf3cWysZDT60zftzHxtrTMNVk"
    )


    //api
    const val TEMPLATE_API = "templates/list"
    const val CREATE_PROFILE_API = "profiles"
    const val LOGIN_API = "oauth/login"
    const val TOKEN_REFRESH = "oauth/refresh"
    const val UPDATE_PROFILE = "profiles/{profileId}"
    const val EDIT_OBJECTIVE = "profiles/{profileId}/objective"
    const val EDIT_EDUCATION = "profiles/{profileId}/qualifications"
    const val EDIT_SKILL = "profiles/{profileId}/skills"
    const val EDIT_EXPERIENCE = "profiles/{profileId}/experiences"
    const val EDIT_INTERESTS = "profiles/{profileId}/interests"
    const val EDIT_LANGUAGE = "profiles/{profileId}/languages"
    const val EDIT_REFERENCE = "profiles/{profileId}/references"
    const val EDIT_ACHIEVEMENTS = "profiles/{profileId}/achievements"
    const val EDIT_PROJECTS = "profiles/{profileId}/projects"
    const val GET_PROFILE_DETAIL = "profiles/{profileId}"
    const val CREATE_COVERLETTER = "cover-letter"
    const val PREVIEW_COVERLETTER_API = "cover-letter/{id}/templates/{templateId}"
    const val PREVIEW_RESUME_API = "profiles/{profileId}/templates/{templateId}"
    const val GETPROFILES_API = "profiles"
    const val SAMPLES_API = "samples"
    const val LOOKUP_API = "lookups"
    const val FCM_API = "fcm-token"
    const val DELTER_PROFILE="profiles/{profileId}"
    const val DELETE_ACCOUNT="users/delete-me"
    const val LOGOUT_ACCOUNT=""


    //lockup Keys
    const val education = "education"
    const val qualifications = "qualifications"
    const val institute = "institute"
    const val skills = "skills"
    const val company = "company"
    const val jobTitle = "jobTitle"
    const val interests = "interests"
    const val languages = "languages"
    const val degree = "degree"
    const val position = "position"
    const val achievements = "achievements"
    const val PDF = "pdf"


    const val COVER_LETTER = "coverLetter"
    const val RESUME = "resume"
    const val PREFS_TOKEN_FILE = "PlayDateApp"
    const val DATA = "data"
    const val DATA_PROFILE = "data"
    const val TOKEN_ID = "token"
    const val IS_LOGGED = "false"

    const val TITLE_DATA = "data"
    const val IMAGE_CODE = 100
    const val IS_RESUME = "IS_RESUME"
    const val DOWNLOAD = "download"
    const val PROFILE = "profile"
    const val FRAGMENT_CALLED = "FRAGMENT"
    const val PROFILE_ID = "profile_id"
    const val IS_FIRST_TIME = "IS_FIRST_TIME"
    const val TEMPLATE_ID = "template"
    const val CAMERA = "CAMERA"
    const val GALLERY = "GALLERY"
    val JPG = "jpg"
    const val CREATE = "CREATE"
    const val IMPORT = "IMPORT"
    const val CHOSE_TEMPLATE = "CHOSE_TEMPLATE"

    const val AUTH_TOKEN = "AUTH_TOKEN"


}