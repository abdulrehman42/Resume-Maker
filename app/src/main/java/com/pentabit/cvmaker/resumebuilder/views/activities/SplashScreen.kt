package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.enableEdgeToEdge
import com.google.android.gms.tasks.Task
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateFailureListener
import com.google.android.ump.ConsentInformation.OnConsentInfoUpdateSuccessListener
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.FormError
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.messaging.FirebaseMessaging
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySplashScreenBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager.isUserConsentsProvided
import com.pentabit.pentabitessentials.ads_manager.ads_callback.AppOpenAdCallbacks
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogType
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.pentabit.pentabitessentials.views.AppsKitSDKApplication
import java.util.concurrent.atomic.AtomicBoolean

class SplashScreen : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    var handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        callFirebaseToken()
    }

    private lateinit var consentInformation: ConsentInformation
    var isContentFiled = AtomicBoolean(false)
    fun requestConsents(testId: String?) {
        consentInformation = UserMessagingPlatform.getConsentInformation(this)
        val debugSettings = ConsentDebugSettings.Builder(this)
            .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            .addTestDeviceHashedId(testId)
            .build()
        val params = ConsentRequestParameters.Builder()
            .setConsentDebugSettings(debugSettings)
            .setTagForUnderAgeOfConsent(false)
            .build()
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            OnConsentInfoUpdateSuccessListener {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this,
                    OnConsentFormDismissedListener { loadAndShowError: FormError? ->
                        if (loadAndShowError != null) {
                            // Consent gathering failed.
                            Log.w(
                                "PTB_AdMobConsentManager", String.format(
                                    "%s: %s",
                                    loadAndShowError.errorCode,
                                    loadAndShowError.message
                                )
                            )
                            isUserConsentsProvided(false)
                            startNextActivity(2000)
                        }
                        if (consentInformation.canRequestAds()) {
                            isUserConsentsProvided(true)
                            startNextActivity(2000)
                        }
                    }
                )
            },
            OnConsentInfoUpdateFailureListener { requestConsentError: FormError ->
                // Consent gathering failed.
                Log.w(
                    "PTB_AdMobConsentManager", String.format(
                        "%s: %s",
                        requestConsentError.errorCode,
                        requestConsentError.message
                    )
                )
                isUserConsentsProvided(false)
                startNextActivity(2000)
            })
        if (consentInformation.canRequestAds()) {
            isUserConsentsProvided(true)
            startNextActivity(2000)
        }
    }

    private fun startNextActivity(value: Int) {
        if (isContentFiled.getAndSet(true)) return
        handler.post {
            val isAppOpenShown =
                AtomicBoolean(false)
            (application as AppsKitSDKApplication).startRequestingAppOpenAd(object :
                AppOpenAdCallbacks() {
                override fun onFailedToLoad() {
                    if (!isAppOpenShown.getAndSet(true)) {
                        AppsKitSDKLogManager.getInstance()
                            .log("PTB_LOG_SPLASH", AppsKitSDKLogType.ERROR, "FailToLoad")
                        handler.postDelayed({ handleNextScreen() }, value.toLong())
                    }
                }

                override fun onLoaded() {}
                override fun onDismiss() {
                    AppsKitSDKLogManager.getInstance()
                        .log("PTB_LOG_SPLASH", AppsKitSDKLogType.ERROR, "DissMiss")
                    handler.postDelayed({ handleNextScreen() }, value.toLong())
                }

                override fun onAdShown() {
                    if (!isAppOpenShown.getAndSet(true)) {
                        AppsKitSDKLogManager.getInstance()
                            .log("PTB_LOG_SPLASH", AppsKitSDKLogType.ERROR, "onAdShown")
                    }
                }

                override fun onAdFailToShow() {
                    handler.postDelayed(
                        { if (!isAppOpenShown.getAndSet(true)) handleNextScreen() },
                        2000
                    )
                }
            }, 6)
            handler.postDelayed({
                if (!isAppOpenShown.getAndSet(true)) {
                    handler.postDelayed({ this.handleNextScreen() }, 100)
                }
            }, 9000)
        }
    }

    private fun handleNextScreen() {
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_INTRO_DONE, false)
        ) {
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplashScreen, BoardingScreen::class.java))
        }
        finish()
    }

    protected fun callFirebaseToken() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task: Task<String?> ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
            }
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
        if (java.lang.Boolean.TRUE == isInternetAvailable) requestConsents("C4FEB06EFDF64FBB3C5334B0239CBEA6")
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.SPLASH
    }
}