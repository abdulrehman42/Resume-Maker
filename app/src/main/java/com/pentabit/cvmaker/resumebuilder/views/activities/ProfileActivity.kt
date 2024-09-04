package com.pentabit.cvmaker.resumebuilder.views.activities

import android.os.Bundle
import android.view.View
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(
            !isInternetAvailable,
            findViewById<View>(R.id.myCoordinatorLayout)
        )
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.PROFILE_LISTING
    }

    override fun onDestroy() {
        super.onDestroy()
        AppsKitSDKPreferencesManager.getInstance()
            .addInPreferences(
                Constants.TEMPLATE_ID,
                ""
            )
    }
}