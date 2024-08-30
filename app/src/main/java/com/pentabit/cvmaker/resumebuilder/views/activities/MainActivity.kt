package com.pentabit.cvmaker.resumebuilder.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdSize
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxDelete
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxLogout
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxPurchase
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxRate
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.link
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.shareAppMethod
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityMainActivtyBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxChooseProfile
import com.pentabit.cvmaker.resumebuilder.utils.ResumeMakerApplication
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager
import com.pentabit.pentabitessentials.firebase.AppsKitSDK
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: ActivityMainActivtyBinding
    lateinit var firebaseRemoteConfig: FirebaseRemoteConfig
    private var flagDrawer = false
    private val scaleFactor = 6f

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        setdrawer()
        onclick()
        refreshToken()
        onObserver()
//        onFcm()
        askNotificationPermission()

    }

    private fun initView() {
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        AppsKitSDKAdsManager.initializeAds(
            Constants.YOUR_IRON_SOURCE_APP_KEY,
            Constants.YOUR_MAX_SDK_KEY,
            Constants.YOUR_HELIUM_APP_ID,
            Constants.YOUR_HELIUM_SIGNATURES,
            Constants.isChildDirected,
            Constants.isSubjectToGDPR,
            Constants.userHasGivenConsent,
            Constants.isCCPAConsent
        )
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_LOGGED, false)
        ) {
            templateViewModel.isLogin.value = true
        }
        AppsKitSDKUtils.setVisibility(
            !AppsKitSDK.getInstance().removeAdsStatus,
            binding.appBarMainActivty.contentmain.adsContraint
        )
        if (AppsKitSDK.getInstance().removeAdsStatus) {
            binding.appBarMainActivty.toolbar.menu.getItem(R.id.action_settings).isVisible = false
        }
        AppsKitSDKAdsManager.showBanner(
            this,
            binding.appBarMainActivty.contentmain.bannerAdd,
            placeholder = ""
        )
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
    }

    private fun onFcm() {
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    templateViewModel.onFCM(task.result)
                } else {
                    val exception = task.exception
                    if (exception != null) {
                        showToast(exception.message)
                    }
                }
            }
    }

    private fun onObserver() {
        templateViewModel.getString.observe(this) {
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.IS_LOGGED, false)
            binding.logout.setText("Login")
        }
        templateViewModel.isLogin.observe(this) {
            if (it) {
                Handler(Looper.getMainLooper()).post {
                    binding.logout.text = "Logout"
                }
            } else {
                Handler(Looper.getMainLooper()).post {
                    binding.logout.text = "Login"
                }
            }
        }
    }


    private fun refreshToken() {
        if (AppsKitSDKPreferencesManager.getInstance()
                .getBooleanPreferences(Constants.IS_LOGGED, false)
        ) {
            templateViewModel.tokenRefresh()
        }
    }

    private fun onclick() {
        binding.logoutBtn.setOnClickListener {
            drawerLayout.closeDrawers()
            if (binding.logout.text.toString().contains("Login")) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Constants.IS_MAIN, true)
                startActivity(intent)
            } else {
                alertboxLogout(this,
                    object : DialogueBoxes.StringValueDialogCallback {
                        override fun onButtonClick(value: String) {
                            if (value == Constants.YES) {
                                AppsKitSDKPreferencesManager.getInstance()
                                    .addInPreferences(Constants.AUTH_TOKEN, "")
                                AppsKitSDKPreferencesManager.getInstance()
                                    .addInPreferences(Constants.IS_LOGGED, false)
//                                sharePref.writeBoolean(Constants.IS_LOGGED, false)
                                templateViewModel.isLogin.value = false
                            }
                        }
                    })
            }
        }
        binding.appBarMainActivty.contentmain.cvResumeBtn.setOnClickListener {
            val intent = Intent(this, ChoiceTemplate::class.java)
            intent.putExtra(Constants.IS_RESUME, true)
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(
                Constants.IS_RESUME,
                true
            )
            startActivity(intent)
        }
        binding.appBarMainActivty.contentmain.coverletterBtn.setOnClickListener {
            val intent = Intent(this, ChoiceTemplate::class.java)
            intent.putExtra(Constants.IS_RESUME, false)
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(
                Constants.IS_RESUME,
                false
            )
            startActivity(intent)
        }
        binding.appBarMainActivty.contentmain.downloadBtn.setOnClickListener {
            if (AppsKitSDKPreferencesManager.getInstance()
                    .getBooleanPreferences(Constants.IS_LOGGED, false)
            ) {
                startActivity(Intent(this, DownloadActivity::class.java))
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
            }
        }

        binding.appBarMainActivty.contentmain.profileBtn.setOnClickListener {
            if (AppsKitSDKPreferencesManager.getInstance()
                    .getBooleanPreferences(Constants.IS_LOGGED, false)
            ) {
                AppsKitSDKPreferencesManager.getInstance()
                    .addInPreferences(Constants.VIEW_PROFILE, true)
                AppsKitSDKPreferencesManager.getInstance().addInPreferences(
                    Constants.FRAGMENT_CALLED,
                    Constants.PROFILE
                )
                startActivity(Intent(this, ProfileActivity::class.java))
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                intent.putExtra(Constants.IS_PROFILE, true)

                startActivity(intent)

            }


        }
        binding.appBarMainActivty.contentmain.removeAddbtn.setOnClickListener {
            startActivity(Intent(this, SubscriptionActivity::class.java))
        }
    }

    private fun setdrawer() {
        binding.appBarMainActivty.toolbar.title = getString(R.string.app_name)
        setSupportActionBar(binding.appBarMainActivty.toolbar)
        drawerLayout = binding.drawerLayout
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener(this)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            binding.appBarMainActivty.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        val mainContent = findViewById<CardView>(R.id.cardviewContent)
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val slideX = drawerView.width * slideOffset
                mainContent.translationX = slideX
                binding.appBarMainActivty.toolbar.translationX = slideX
                mainContent.scaleX = 1 - slideOffset / scaleFactor
                mainContent.scaleY = 1 - slideOffset / scaleFactor
                mainContent.radius = 30f
            }

            @SuppressLint("RestrictedApi")
            override fun onDrawerOpened(drawerView: View) {
                toggle.onDrawerOpened(drawerView)
                getActivity(this@MainActivity)!!.windowManager.defaultDisplay

                flagDrawer = true
                mainContent.radius = 30f
            }

            override fun onDrawerClosed(drawerView: View) {
                mainContent.radius = 0f
                binding.appBarMainActivty.toolbar.translationX = 0f

                flagDrawer = false
            }

            override fun onDrawerStateChanged(newState: Int) {
            }
        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_rate -> alertboxRate(this)
            R.id.nav_share -> shareAppMethod(this)
            R.id.nav_delete -> alertboxDelete(this,
                object : DialogueBoxes.StringValueDialogCallback {
                    override fun onButtonClick(value: String) {
                        if (value == Constants.YES) {
                            templateViewModel.onDeleteMe()
                        }
                    }
                })

            R.id.nav_retore -> {
                startActivity(Intent(this, SubscriptionActivity::class.java))
            }

            R.id.nav_privacy -> link("https://www.pentabitapps.com/privacy-policy", this)
            R.id.nav_term -> link("https://www.pentabitapps.com/terms-of-use", this)
            R.id.nav_more -> link(
                "https://play.google.com/store/apps/developer?id=Pentabit Apps",
                this
            )
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun attachViewMode() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activty, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {
                alertboxPurchase(this,
                    object : DialogueBoxes.DialogCallback {
                        override fun onButtonClick(isConfirmed: Boolean) {
                            if (isConfirmed) {
                                startActivity(
                                    Intent(
                                        this@MainActivity,
                                        SubscriptionActivity::class.java
                                    )
                                )
                            }
                        }
                    })
            }
        }
        return super.onOptionsItemSelected(item)
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

    private fun askNotificationPermission() {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (java.lang.Boolean.TRUE == isGranted) {
            }
        }


    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.HOME
    }

}