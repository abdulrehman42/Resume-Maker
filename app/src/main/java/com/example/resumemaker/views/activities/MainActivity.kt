package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityMainActivtyBinding
import com.example.resumemaker.utils.DialogueBoxes.alertboxDelete
import com.example.resumemaker.utils.DialogueBoxes.alertboxLogout
import com.example.resumemaker.utils.DialogueBoxes.alertboxPurchase
import com.example.resumemaker.utils.DialogueBoxes.alertboxRate
import com.example.resumemaker.utils.DialogueBoxes.link
import com.example.resumemaker.utils.DialogueBoxes.shareAppMethod
import com.example.resumemaker.views.fragments.HomeFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainActivtyBinding
    private var flagDrawer = false
    private val scaleFactor = 6f

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        bottomNavigationColor()
        binding = ActivityMainActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setdrawer()
        onclick()

        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

    }

    private fun onclick() {
        binding.logoutBtn.setOnClickListener{
            drawerLayout.closeDrawers()
            alertboxLogout(this)
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
                // Optionally handle the state changes
            }
        })

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_rate -> alertboxRate(this)
            R.id.nav_share-> shareAppMethod(this)
            R.id.nav_delete-> alertboxDelete(this)
            R.id.nav_privacy->link("https://www.pentabitapps.com/privacy-policy",this)
            R.id.nav_term->link("https://www.pentabitapps.com/terms-of-use",this)
            R.id.nav_more-> link("https://play.google.com/store/apps/developer?id=Pentabit Apps",this)

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
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activty, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main_activty)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.action_settings-> {alertboxPurchase(this)}
        }
        return super.onOptionsItemSelected(item)
    }


    fun replaceFragment(fragment: Fragment) {
        val beginTranstion: FragmentTransaction = supportFragmentManager.beginTransaction()
        beginTranstion.replace(R.id.nav_host_fragment_content_main_activty, fragment).commit()
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