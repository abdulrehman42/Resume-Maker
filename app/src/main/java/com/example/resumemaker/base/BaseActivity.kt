package com.example.resumemaker.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.resumemaker.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

   // var progressBar: KProgressHUD? = null

     lateinit var options: NavOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationColor()
        enableEdgeToEdge()
        options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

    }

    abstract fun attachViewMode()
    fun replaceAddDetailFragment(action: Int) {
        Navigation.findNavController(this, R.id.viewPager_container).navigate(action, null, options)
    }
    fun replaceChoiceFragment(action: Int) {
        Navigation.findNavController(this, R.id.choiceHostFragment).navigate(action, null, options)
    }
    fun replaceChoiceFragment(action: Int,bundle: Bundle) {
        Navigation.findNavController(this, R.id.choiceHostFragment).navigate(action,bundle , options)
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
  fun bottomNavigationColor()
    {
        window.navigationBarColor = ContextCompat.getColor(this,R.color.navy_blue)
    }

    fun showSnackbar(view: View, message: String?) {
        message?.let {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        } ?: kotlin.run {
            Snackbar.make(view, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT)
                .show()
        }

    }
    private fun enableEdgeToEdge() {
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