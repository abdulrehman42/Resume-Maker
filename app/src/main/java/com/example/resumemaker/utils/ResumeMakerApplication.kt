package com.example.resumemaker.utils

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.resumemaker.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.pentabitessentials.views.AppsKitSDKApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ResumeMakerApplication : AppsKitSDKApplication() {
    var sharePref: SharePref? = null

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    override fun setAKSDefaultConfigs(): String {
       return ""
    }

    override fun fetchFirebaseDefaults(): Int {
        return R.xml.remote_config_defaults
    }

    override fun isTestMode(): Boolean {
        return true
    }

    override fun isDevMode(): Boolean {
        return true
    }

    override fun onConfigsReadyToUse(p0: FirebaseRemoteConfig?) {

    }

    private fun initialize() {
        SharePref.init(this)
        sharePref = SharePref.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}