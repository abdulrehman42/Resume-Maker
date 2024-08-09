package com.example.resumemaker.views.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Display
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.cardview.widget.CardView
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
import com.example.resumemaker.views.fragments.HomeFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainActivtyBinding
    private var flagDrawer = false
    private val scaleFactor = 6f

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMainActivty.toolbar)


        drawerLayout= binding.drawerLayout
        drawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));

        val navView: NavigationView = binding.navView
        navView.setNavigationItemSelectedListener (this)
        val toggle =ActionBarDrawerToggle(this,drawerLayout,binding.appBarMainActivty.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        if (savedInstanceState==null){
            replaceFragment(HomeFragment())
        }
        val mainContent=findViewById<CardView>(R.id.cardviewContent)
        drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                val slideX = drawerView.width * slideOffset
                mainContent.translationX = slideX
                binding.appBarMainActivty.toolbar.translationX = slideX
                mainContent.scaleX = 1 - slideOffset / scaleFactor
                mainContent.scaleY = 1 - slideOffset / scaleFactor
                mainContent.radius = 30f
            }

            override fun onDrawerOpened(drawerView: View) {
                toggle.onDrawerOpened(drawerView)
                 getActivity(this@MainActivity)!!. windowManager.defaultDisplay

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
            R.id.nav_rate -> Toast.makeText(this, "rate us", Toast.LENGTH_SHORT).show()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun attachViewMode() {

    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START)
        }else{
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



    fun replaceFragment(fragment: Fragment){
        val beginTranstion:FragmentTransaction=supportFragmentManager.beginTransaction()
        beginTranstion.replace(R.id.nav_host_fragment_content_main_activty,fragment).commit()
    }
    @SuppressLint("ResourceAsColor")
    override fun onStart() {
        super.onStart()
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN)
        window.statusBarColor=getResources().getColor(R.color.navy_blue)
    }


}