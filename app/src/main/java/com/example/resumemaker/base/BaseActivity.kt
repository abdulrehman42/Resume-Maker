package com.example.resumemaker.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.resumemaker.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() {

   // var progressBar: KProgressHUD? = null

    private lateinit var options: NavOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*progressBar = KProgressHUD.create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setCancellable(true)
            .setBackgroundColor(Color.TRANSPARENT)
            .setAnimationSpeed(1)
            .setDimAmount(0.5f)*/

        options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setEnterAnim(R.anim.fade_in)
            .setExitAnim(R.anim.fade_out)
            .setPopEnterAnim(R.anim.fade_in)
            .setPopExitAnim(R.anim.fade_out)
            .build()

    }


    /*fun showLoadingBar() {
        if (progressBar != null && !progressBar!!.isShowing)
            progressBar?.show()
    }

    fun hideLoadingBar() {
        if (progressBar != null && progressBar!!.isShowing)
            progressBar?.dismiss()
    }*/

    abstract fun attachViewMode()

    fun replaceChoiceFragment(action: Int) {
        Navigation.findNavController(this, R.id.choiceHostFragment).navigate(action, null, options)
    }
    /*fun replaceMainFragment(action: Int) {
        Navigation.findNavController(this, R.id.fragmentMain).navigate(action, null, options)
    }

    fun replaceMainFragment(action: Int, bundle: Bundle) {
        Navigation.findNavController(this, R.id.fragmentMain)
            .navigate(action, bundle, options)
    }

    fun replaceAndRemoveMainFragment(action1: Int, action2: Int, bundle: Bundle? = null) {
        Navigation.findNavController(this, R.id.fragmentMain).popBackStack(action1, true)
        Navigation.findNavController(this, R.id.fragmentMain)
            .navigate(action2, bundle, options)

    }

    fun replaceFragmentInAuth(action: Int) {          // fragment id not nav grpah
        Navigation.findNavController(this, R.id.authHostFragment).navigate(action, null, options)
    }

    fun replaceFragmentInAuth(action: Int, bundle: Bundle) {
        Navigation.findNavController(this, R.id.authHostFragment).navigate(action, bundle, options)
    }

    fun replaceAndRemoveFragmentInAuth(action1: Int, action2: Int, bundle: Bundle? = null) {
        Navigation.findNavController(this, R.id.authHostFragment).popBackStack(action1, true)
        Navigation.findNavController(this, R.id.authHostFragment).navigate(action2, bundle, options)

    }
*/

    fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        } ?: kotlin.run {
            Toast.makeText(this, getString(R.string.something_went_wrong), Toast.LENGTH_SHORT)
                .show()
        }

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