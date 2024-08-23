package com.pentabit.cvmaker.resumebuilder.api.http

interface ResponseCallback {
    fun onSuccess(message: String?, data: Any?)

    fun onFailure(errorMessage: String?)
}