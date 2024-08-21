package com.example.resumemaker.utils

object Constants {
    const val TEMPLATE_ID="template"
    const val BASE_MEDIA_URL="https://n0ngl4zx.tinifycdn.com/"
    const val BASE_URL= "http://35.164.228.120:9000/v1/"
    const val TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjBlYWM5Mjc3LWNjYjgtNDAyOC1hM2UxLTYzNWI1OTM2NGU4NyIsIm5hbWUiOm51bGwsImVtYWlsIjoiYS5yZWhtYW5AcGVudGFiaXRsYWIuY29tIiwib2F1dGhJZCI6InN0cmluZyIsInBob25lTGFuZ3VhZ2UiOm51bGwsIm9hdXRoUHJvdmlkZXIiOiJsaW5rZWRJbiIsImlzRGVsZXRlZCI6ZmFsc2UsInByb2ZpbGVzIjpbNzM4OSw3Mzk3LDc0MDIsNzQyMl0sInVzZXJUeXBlIjoidXNlciIsImlhdCI6MTcyNDI3MjQ3MH0.fC4IYY5fItEi4aT-JNmj4A0zogcv1PqBWRT_aymHYVs"

    //api
    const val TEMPLATE_API="templates/list"
    const val CREATE_PROFILE_API="profiles"
    const val UPDATE_PROFILE="profiles/{profileId}"
    const val EDIT_OBJECTIVE="profiles/{profileId}/objective"
    const val EDIT_EDUCATION="profiles/{profileId}/qualifications"
    const val EDIT_SKILL="profiles/{profileId}/skills"
    const val EDIT_EXPERIENCE="profiles/{profileId}/experiences"
    const val EDIT_INTERESTS="profiles/{profileId}/interests"
    const val EDIT_LANGUAGE="profiles/{profileId}/languages"
    const val EDIT_REFERENCE="profiles/{profileId}/references"
    const val EDIT_ACHIEVEMENTS="profiles/{profileId}/achievements"
    const val EDIT_PROJECTS="profiles/{profileId}/projects"
    const val GET_PROFILE_DETAIL="profiles/{profileId}"
    const val CREATE_COVERLETTER="cover-letter"
    const val PREVIEW_COVERLETTER_API="cover-letter/{id}/templates/{templateId}"
    const val PREVIEW_RESUME_API="profiles/{profileId}/templates/{templateId}"
    const val GETPROFILES_API="profiles"

    const val SAMPLES_API="samples"


    const val COVER_LETTER="coverLetter"
    const val RESUME="resume"
    const val PREFS_TOKEN_FILE = "PlayDateApp"
    const val DATA="data"
    const val DATA_PROFILE="data"

    const val TITLE_DATA="data"
    const val IMAGE_CODE = 100
    const val IS_RESUME="IS_RESUME"
    const val DOWNLOAD="download"
    const val PROFILE="profile"
    const val FRAGMENT_CALLED="FRAGMENT"
    const val PROFILE_ID="profile_id"

    const val CAMERA="CAMERA"
    const val GALLERY="GALLERY"

    const val CREATE="CREATE"
    const val IMPORT="IMPORT"
    const val CHOSE_TEMPLATE="CHOSE_TEMPLATE"


}