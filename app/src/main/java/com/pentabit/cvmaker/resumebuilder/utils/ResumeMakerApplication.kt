package com.pentabit.cvmaker.resumebuilder.utils

import androidx.appcompat.app.AppCompatDelegate
import com.pentabit.cvmaker.resumebuilder.R
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.utils.Constants.GUEST_TOKEN
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.views.AppsKitSDKApplication
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class ResumeMakerApplication : AppsKitSDKApplication() {
    var sharePref: SharePref? = null

    companion object {
        lateinit var instance: ResumeMakerApplication
    }



    override fun onCreate() {
        super.onCreate()
        instance = this@ResumeMakerApplication
        Paper.init(this)
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

    override fun onConfigsReadyToUse(firebaseRemoteConfig: FirebaseRemoteConfig) {
        val token: String = firebaseRemoteConfig.getString(GUEST_TOKEN)
//        Constants.TOKEN = token
        AppsKitSDKPreferencesManager.getInstance()
            .addInPreferences(GUEST_TOKEN, token)
    }

    private fun initialize() {
        SharePref.init(this)
        sharePref = SharePref.getInstance()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}