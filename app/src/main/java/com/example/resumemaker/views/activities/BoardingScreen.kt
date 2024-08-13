package com.example.resumemaker.views.activities

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.resumemaker.R
import com.example.resumemaker.base.BaseActivity
import com.example.resumemaker.databinding.ActivityBoardingScreenBinding
import com.example.resumemaker.utils.Helper
import com.example.resumemaker.views.adapter.ImagePagerAdapter


class BoardingScreen : BaseActivity() {
    private var onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            Helper.updateCircleMarker(binding, position)
        }
    }
    lateinit var binding: ActivityBoardingScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityBoardingScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initview()
        onclick()
    }

    override fun attachViewMode() {
        TODO("Not yet implemented")
    }

    private fun onclick() {
        binding.nextbtn.setOnClickListener {
            if (binding.viewPager.currentItem==3)
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                binding.viewPager.setCurrentItem(getItem(+1), true)
            }
        }
        binding.skipbtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun getItem(i: Int): Int {
        return binding.viewPager.getCurrentItem() + i
    }

    private fun initview() {
        val numberOfScreens = resources.getStringArray(R.array.on_boarding_titles).size
        binding.choiceMain.makeStatusBarTransparent()
        val onBoardingAdapter = ImagePagerAdapter(this, numberOfScreens)
        binding.viewPager.adapter = onBoardingAdapter
        binding.viewPager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
    }

    override fun onDestroy() {
        binding.viewPager.unregisterOnPageChangeCallback(onBoardingPageChangeCallback)
        super.onDestroy()
    }

    private fun View.makeStatusBarTransparent() {
        this.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
    }

    /*override fun onStart() {
        super.onStart()
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
    }*/
    private fun enableEdgeToEdge() {
        // Set the decor view to enable full-screen layout
        val window = window

        // Make sure that the content extends into the system bars (status bar and navigation bar)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        // Set system bars to be transparent
        window.statusBarColor = Color.TRANSPARENT
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