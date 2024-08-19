package com.example.resumemaker.base

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

    private lateinit var options: NavOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bottomNavigationColor()

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
  private fun bottomNavigationColor()
    {
        getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.navy_blue))
    }

    fun showSnackbar(view: View, message: String?) {
        message?.let {
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
        } ?: kotlin.run {
            Snackbar.make(view, getString(R.string.something_went_wrong), Snackbar.LENGTH_SHORT)
                .show()
        }

    }


}