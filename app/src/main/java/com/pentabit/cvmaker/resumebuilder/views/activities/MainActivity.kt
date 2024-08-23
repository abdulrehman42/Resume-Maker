package com.pentabit.cvmaker.resumebuilder.views.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxDelete
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxLogout
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxPurchase
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.alertboxRate
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.link
import com.pentabit.cvmaker.resumebuilder.utils.DialogueBoxes.shareAppMethod
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityMainActivtyBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ResumeMakerApplication
import com.pentabit.cvmaker.resumebuilder.viewmodels.TemplateViewModel
import dagger.hilt.android.AndroidEntryPoint

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
        templateViewModel = ViewModelProvider(this)[TemplateViewModel::class.java]
        sharePref.writeBoolean(Constants.IS_FIRST_TIME, false)
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        setdrawer()
        onclick()
        refreshToken()
        onObserver()


    }

    private fun onObserver() {
        templateViewModel.loginResponse.observe(this) {

        }
    }


    private fun refreshToken() {
        if (sharePref.readBoolean(Constants.IS_LOGGED, false)) {
            templateViewModel.tokenRefresh()
        }

    }

    private fun onclick() {
        binding.logoutBtn.setOnClickListener {
            drawerLayout.closeDrawers()
            alertboxLogout(this)
        }
        binding.appBarMainActivty.contentmain.cvResumeBtn.setOnClickListener {
            val intent = Intent(this, ChoiceTemplate::class.java)
            intent.putExtra(com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME, true)
            sharePref.writeBoolean(
                com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME,
                true
            )
            startActivity(intent)
        }
        binding.appBarMainActivty.contentmain.coverletterBtn.setOnClickListener {
            val intent = Intent(this, ChoiceTemplate::class.java)
            intent.putExtra(com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME, false)
            sharePref.writeBoolean(
                com.pentabit.cvmaker.resumebuilder.utils.Constants.IS_RESUME,
                false
            )
            startActivity(intent)
        }
        binding.appBarMainActivty.contentmain.downloadBtn.setOnClickListener {
            startActivity(Intent(this, DownloadActivity::class.java))
        }
        binding.appBarMainActivty.contentmain.profileBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
//            sharePref.writeString(
//                com.pentabit.cvmaker.resumebuilder.utils.Constants.FRAGMENT_CALLED,
//                com.pentabit.cvmaker.resumebuilder.utils.Constants.PROFILE
//            )
//            startActivity(Intent(this, ProfileActivity::class.java))
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
            R.id.nav_delete -> alertboxDelete(this)
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
                alertboxPurchase(this)
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

}