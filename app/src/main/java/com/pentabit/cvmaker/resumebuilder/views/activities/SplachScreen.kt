package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySplashScreenBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.addToken
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplachScreen : BaseActivity() {
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var binding: ActivitySplashScreenBinding
    lateinit var templateViewModel: TemplateViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

        if (sharePref.readBoolean(Constants.IS_FIRST_TIME, true)) {
            startActivity(Intent(this@SplachScreen, BoardingScreen::class.java))
            finish()
        } else {
            startActivity(Intent(this@SplachScreen, MainActivity::class.java))
            finish()
        }
    }


    override fun attachViewMode() {
    }
}