package com.example.resumemaker.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ResumeMakerApplication:Application() {
    var sharePref: SharePref? = null

    override fun onCreate() {
        super.onCreate()
        //Paper.init(this)
        initialize()
    }

    private fun initialize() {
        SharePref.init(this)
        sharePref = SharePref.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}