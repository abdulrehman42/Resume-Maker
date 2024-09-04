package com.pentabit.cvmaker.resumebuilder.base

import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity

abstract class BaseActivity : AdBaseActivity() {

    lateinit var options: NavOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()
        showInternetConnectivity(true)
    }


    private fun enableEdgeToEdge() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.navy_blue)
    }

    fun replaceProfileFragment(action: Int) {
        Navigation.findNavController(this, R.id.profileHostFragment).navigate(action, null, options)
    }

    fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                .show()
        }

    }

    fun bottomNavigationColor() {
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navy_blue)
    }

    override fun isPortrait(): Boolean {
        return true
    }

}