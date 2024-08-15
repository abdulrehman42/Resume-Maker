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
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ChoiceTemplate : BaseActivity() {
    lateinit var binding: ActivityChoiceTemplateBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding=ActivityChoiceTemplateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.choiceHostFragment)
        if (intent.getStringExtra(Constants.FRAGMENT_NAME).toString()==Constants.RETURN_FROM_PROFILE)
        {
            navController.navigate(R.id.nav_add_detail)
        }
        if (intent.getStringExtra(Constants.FRAGMENT_NAME).toString()==Constants.PROFILE)
        {
            navController.navigate(R.id.nav_profileFragment)
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
        window.statusBarColor = ContextCompat.getColor(this,R.color.navy_blue)
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