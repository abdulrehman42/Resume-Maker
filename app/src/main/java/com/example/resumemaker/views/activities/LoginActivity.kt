package com.example.resumemaker.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.FragmentLoginBinding
import com.example.resumemaker.models.request.addDetailResume.LoginRequestModel
import com.example.resumemaker.utils.Constants
import com.example.resumemaker.viewmodels.TemplateViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    lateinit var binding: FragmentLoginBinding
    val templateViewModel by viewModels<TemplateViewModel>()
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var googleoption: GoogleSignInOptions
    lateinit var googleclient: GoogleSignInClient
    var isResume = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.navy_blue)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isResume = intent.getBooleanExtra(Constants.IS_RESUME, false)
        binding.loginbtnlinearbtn.setOnClickListener {
           sign_process()
        }
        liveDataObserve()
    }

    private fun liveDataObserve() {
        templateViewModel.getString.observe(this){
            if (isResume) {
                val intent = Intent(this, ProfileActivity::class.java)
                intent.putExtra(Constants.IS_RESUME, Constants.PROFILE)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(Intent(this, ChoiceTemplate::class.java))
                intent.putExtra(Constants.IS_RESUME, isResume)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun sign_process() {
        googleoption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(resources.getString(R.string.default_web_client_id))
            .requestEmail().build()
        googleclient = GoogleSignIn.getClient(this, googleoption)
        googleclient.revokeAccess()
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
                    if (googleSignInAccount != null) {
                        val authCredential: AuthCredential = GoogleAuthProvider.getCredential(
                            googleSignInAccount.idToken, null
                        )
                        auth.signInWithCredential(authCredential)
                            .addOnCompleteListener(this) { task1 ->
                                if (task1.isSuccessful) {
                                    makeApiCall(task1.result.user!!.email.toString())
                                } else {
                                    showToast(task1.exception?.message)
                                }

                            }
                    }
                } catch (e: ApiException) {
                    showToast(task.exception?.message)
                }
            }else{
                if (isResume) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    sharePref.writeString(Constants.FRAGMENT_CALLED,Constants.RESUME)
                   // intent.putExtra(Constants.IS_RESUME, Constants.PROFILE)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(Intent(this, AddDetailResume::class.java))
                    intent.putExtra(Constants.IS_RESUME, isResume)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }

    fun makeApiCall(email: String) {
        templateViewModel.loginRequest(LoginRequestModel( email,"","","03034241566"))
    }
    override fun attachViewMode() {

    }
}