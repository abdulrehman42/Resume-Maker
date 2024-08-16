package com.example.resumemaker.views.activities

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityChoiceTemplateBinding
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.views.fragments.addDetailResume.AddAchievementsFRagment
import com.example.resumemaker.views.fragments.addDetailResume.AddEducation
import com.example.resumemaker.views.fragments.addDetailResume.AddExperienceFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddInterestFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddLanguageFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddProjectFragment
import com.example.resumemaker.views.fragments.addDetailResume.AddSkillFragment
import com.example.resumemaker.views.fragments.addDetailResume.EducationFragment
import com.example.resumemaker.views.fragments.addDetailResume.InformationFragment
import com.example.resumemaker.views.fragments.addDetailResume.InterestFragment
import com.example.resumemaker.views.fragments.addDetailResume.LanguageFragment
import com.example.resumemaker.views.fragments.addDetailResume.ReferrenceFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChoiceTemplate : BaseActivity() {
    lateinit var binding: ActivityChoiceTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivityChoiceTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.choiceHostFragment)
        val fragmentsToLoad = intent.getStringExtra(Constants.FRAGMENT_NAME)
        if (savedInstanceState == null && fragmentsToLoad != null) {
            val fragment = when (fragmentsToLoad) {
                Constants.EDUCATION -> R.id.nav_add_education
                Constants.SKILL -> R.id.nav_add_skill
                Constants.EXPERIENCE -> R.id.nav_add_experience
                Constants.REFERRENCE -> R.id.nav_add_referrence
                Constants.INTEREST -> R.id.nav_Add_interest
                Constants.LANGUAGE -> R.id.nav_add_language
                Constants.PROJECT -> R.id.nav_add_projects
                Constants.ACHIEVEMNT -> R.id.nav_add_achievement
                Constants.PREVIEW_RESUME->R.id.nav_add_preview_resume
                Constants.PROFILE->R.id.nav_add_preview_resume
                else -> return
            }

            navController.navigate(fragment)
        }

    }

    override fun attachViewMode() {

    }


    private fun enableEdgeToEdge() {
// Set the decor view to enable full-screen layout
        val window = window

// Make sure that the content extends into the system bars (status bar and navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

// Set system bars to be transparent
        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        window.navigationBarColor = Color.TRANSPARENT

// Optionally, handle light or dark mode for the status bar icons
        var flags = window.decorView.systemUiVisibility
        flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // Light status bar (dark icons)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            flags =
                flags or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR // Light navigation bar (dark icons)
        }
        window.decorView.systemUiVisibility = flags
    }


}