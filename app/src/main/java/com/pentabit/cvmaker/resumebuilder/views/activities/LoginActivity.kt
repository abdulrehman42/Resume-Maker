package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.gson.reflect.TypeToken
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.FragmentLoginBinding
import com.pentabit.cvmaker.resumebuilder.json.JSONManager
import com.pentabit.cvmaker.resumebuilder.json.JWTUtils
import com.pentabit.cvmaker.resumebuilder.models.User
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    lateinit var binding: FragmentLoginBinding
    val templateViewModel by viewModels<TemplateViewModel>()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleoption: GoogleSignInOptions
    lateinit var googleclient: GoogleSignInClient
    var isResume = false
    var isMain = false
    var isProfile=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isResume = intent.getBooleanExtra(Constants.IS_RESUME, false)
        isMain = intent.getBooleanExtra(Constants.IS_MAIN, false)
        isProfile=intent.getBooleanExtra(Constants.IS_PROFILE,false)
        binding.loginbtnlinearbtn.setOnClickListener {
            sign_process()
        }
        liveDataObserve()
    }

    private fun liveDataObserve() {
        templateViewModel.loadingState.observe(this) {
            AppsKitSDKUtils.setVisibility(it, binding.loader)

        }
        templateViewModel.loginResponse.observe(this) {
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.IS_LOGGED, true)
            templateViewModel.isLogin.value = true
            val user = JSONManager.getInstance()
                .convertJsonToObject(
                    JWTUtils.decoded(it),
                    object : TypeToken<User>() {}.type
                ) as User
            AppsKitSDKPreferencesManager.getInstance()
                .addInPreferences(Constants.USER_ID, user.id)
            if (isProfile)
            {
                navigateToProfileActivitySimple()
            }else if (isResume) {
                navigateToProfileActivity()
            } else if (isMain) {
                finish()
            } else {
                navigateToAddDetailResumeActivity()
            }
        }
    }

    private fun sign_process() {
        googleoption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleclient = GoogleSignIn.getClient(this, googleoption)
        googleclient.revokeAccess() // Revoke previous access to ensure a fresh sign-in
        sign_in()
    }

    private fun sign_in() {
        val intent: Intent = googleclient.signInIntent
        startActivityForResult(intent, 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                try {
                    val googleSignInAccount: GoogleSignInAccount =
                        task.getResult(ApiException::class.java)
                    googleSignInAccount?.let { account ->
                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            account.idToken, null
                        )
                        auth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task1 ->
                                if (task1.isSuccessful) {
                                    try {
                                        val email = task1.result?.user?.email ?: run {
                                            Log.e("SignInError", "User email is null")
                                            "No email available"
                                        }
                                        email.let {

                                            makeApiCall(email, googleSignInAccount.idToken)

                                        }
                                    } catch (e: Exception) {
                                        task1.exception?.let { exception ->
                                            showToast(exception.message ?: "Authentication failed")
                                        } ?: run {
                                            showToast("Authentication failed with unknown error")
                                        }
                                    }
                                } else {
                                    task1.exception?.let { exception ->
                                        showToast(exception.message ?: "Authentication failed")
                                    } ?: run {
                                        showToast("Authentication failed with unknown error")
                                    }
                                }
                            }
                    }
                } catch (e: ApiException) {
                    showToast(e.message ?: "Google sign-in error")
                }
            }
        }
    }

    private fun navigateToProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        AppsKitSDKPreferencesManager.getInstance()
            .addInPreferences(Constants.FRAGMENT_CALLED, Constants.RESUME)
        startActivity(intent)
        finish()
    }
    private fun navigateToProfileActivitySimple() {
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.VIEW_PROFILE,true)
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToAddDetailResumeActivity() {
        val intent = Intent(this, ChoiceTemplate::class.java)
        intent.putExtra(Constants.Is_CoverLtter, true)
        startActivity(intent)
        finish()
    }

    fun makeApiCall(email: String, idToken: String?) {
        templateViewModel.loginRequest(
            email,
            "email"
        )
    }

    private fun View.makeStatusBarTransparent() {
        this.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.LOGIN
    }
}