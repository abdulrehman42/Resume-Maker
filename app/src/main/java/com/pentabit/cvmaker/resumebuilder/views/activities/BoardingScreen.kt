package com.pentabit.cvmaker.resumebuilder.views.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.viewpager2.widget.ViewPager2
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityBoardingScreenBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.Helper
import com.pentabit.cvmaker.resumebuilder.views.adapter.ImagePagerAdapter
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager

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

    private fun mpveToNextActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(Constants.IS_INTRO_DONE, true)
        finish()
    }

    private fun onclick() {
        binding.nextbtn.setOnClickListener {
            if (binding.viewPager.currentItem == 3) {
                mpveToNextActivity()
            } else {
                binding.viewPager.setCurrentItem(getItem(+1), true)
            }
        }
        binding.skipbtn.setOnClickListener {
            mpveToNextActivity()
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


}