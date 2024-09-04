package com.pentabit.cvmaker.resumebuilder.application

import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.BuildConfig
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.utils.Constants.AKS_CONFIGS
import com.pentabit.cvmaker.resumebuilder.utils.Constants.GUEST_TOKEN
import com.pentabit.cvmaker.resumebuilder.utils.Constants.HELIUM_APP_ID
import com.pentabit.cvmaker.resumebuilder.utils.Constants.HELIUM_SIGNATURES
import com.pentabit.cvmaker.resumebuilder.utils.Constants.IRON_SOURCE_APP_KEY
import com.pentabit.cvmaker.resumebuilder.utils.Constants.MAX_SDK_KEY
import com.pentabit.cvmaker.resumebuilder.utils.Constants.SHOW_LOADING_BEFORE_INTERSTITIAL
import com.pentabit.cvmaker.resumebuilder.utils.FreeTaskManager
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager.initializeAds
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.views.AppsKitSDKApplication
import dagger.hilt.android.HiltAndroidApp
import io.paperdb.Paper

@HiltAndroidApp
class ResumeMakerApplication : AppsKitSDKApplication() {

    companion object {
        lateinit var instance: ResumeMakerApplication
    }

    private var isInterstitialShown = false
    private var isFragInterstitialShown = false


    override fun onCreate() {
        super.onCreate()
        instance = this@ResumeMakerApplication
        Paper.init(this)
        initialize()
    }

    override fun setAKSDefaultConfigs(): String {
        return AKS_CONFIGS
    }

    override fun fetchFirebaseDefaults(): Int {
        return R.xml.remote_config_defaults
    }

    override fun isTestMode(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun isDevMode(): Boolean {
        return BuildConfig.DEBUG
    }

    override fun onConfigsReadyToUse(firebaseRemoteConfig: FirebaseRemoteConfig) {
        val token: String = firebaseRemoteConfig.getString(GUEST_TOKEN)
        AppsKitSDKPreferencesManager.getInstance()
            .addInPreferences(GUEST_TOKEN, token)
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(
            SHOW_LOADING_BEFORE_INTERSTITIAL,
            firebaseRemoteConfig.getBoolean(SHOW_LOADING_BEFORE_INTERSTITIAL)
        )
    }

    private fun initialize() {
        initializeAds(
            IRON_SOURCE_APP_KEY, MAX_SDK_KEY, HELIUM_APP_ID, HELIUM_SIGNATURES,
            false, false, true, false
        )
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }


    fun isInterstitialShown(): Boolean {
        return isInterstitialShown
    }

    fun setInterstitialShownState(shown: Boolean) {
        isInterstitialShown = shown
    }

    fun isFragInterstitialShown(): Boolean {
        return isFragInterstitialShown
    }

    fun setFragInterstitialShownState(shown: Boolean) {
        isFragInterstitialShown = shown
    }


}