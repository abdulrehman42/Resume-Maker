package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySplashScreenBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplachScreen : BaseActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    lateinit var templateViewModel: TemplateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        AppsKitSDKAdsManager.isUserConsentsProvided(true)
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_INTRO_DONE, false)
        ) {
            startActivity(Intent(this@SplachScreen, MainActivity::class.java))
        } else {
            startActivity(Intent(this@SplachScreen, BoardingScreen::class.java))
        }
        finish()
    }


    override fun attachViewMode() {
    }
}