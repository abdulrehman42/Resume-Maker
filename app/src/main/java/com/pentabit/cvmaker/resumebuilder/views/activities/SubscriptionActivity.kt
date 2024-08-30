package com.pentabit.cvmaker.resumebuilder.views.activities

import android.graphics.Paint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.pentabit.cvmaker.resumebuilder.R
import com.pentabit.cvmaker.resumebuilder.base.BaseActivity
import com.pentabit.cvmaker.resumebuilder.databinding.ActivitySubscriptionBinding
import com.pentabit.cvmaker.resumebuilder.utils.Constants
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs
import com.pentabit.galleryvault.utils.inapp.InAppPurchase
import com.pentabit.pentabitessentials.firebase.AppsKitSDK
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils


class SubscriptionActivity : BaseActivity() {
    lateinit var binding: ActivitySubscriptionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubscriptionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.term.paintFlags = binding.term.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        binding.cancelBtn.setOnClickListener {
            finish()
        }
        binding.purchaseConstraint.setOnClickListener {
            InAppPurchase.startBillingFlow(
                this, Constants.REMOVE_ADS_ID, false
            ) { removeAds, p1, p2, p3 ->
                if (removeAds) {
                    AppsKitSDK.getInstance().removeAdsStatus = true
                    AppsKitSDKUtils.makeToast("Remove Ads Purchased Successfully")
                }
                finish()
            }
        }
        binding.term.setOnClickListener {
            AppsKitSDKUtils.actionOnTermsOfUse(this@SubscriptionActivity)
            // link("https://www.pentabitapps.com/terms-of-use", this)
        }

    }

    override fun attachViewMode() {

    }

    override fun onInternetConnectivityChange(isInternetAvailable: Boolean) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout)
    }

    override fun getScreenId(): ScreenIDs {
        return ScreenIDs.SUBSCRIPTION
    }
}