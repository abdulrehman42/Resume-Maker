package com.pentabit.cvmaker.resumebuilder.views.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager;
import com.pentabit.pentabitessentials.ads_manager.ads_callback.AdsCallback;
import com.pentabit.pentabitessentials.views.AppsKitSDKBaseActivity;

public abstract class AdBaseActivity extends AppsKitSDKBaseActivity {

    private boolean isVisible = false;
    protected ScreenIDs currentFragment = null;
    protected String placeHolder;
    protected ScreenIDs screen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeHolder = getAdsKey();
        screen = getScreenId();
        MyApplication.getInstance().setInterstitialShownState(false);
        if (isPortrait())
            restrictPortraitOnly();
        if (!FirebaseConfigManager.getInstance().isScreenSharingEnabled())
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    public void setCurrentFragment(ScreenIDs currentFragment) {
        this.currentFragment = currentFragment;
    }

    public void askAdOnFragment(ScreenIDs currentFragment) {
        setCurrentFragment(currentFragment);
        requestInterstitial();
        MyApplication.getInstance().setFragInterstitialShownState(true);
        MyApplication.getInstance().setInterstitialShownState(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        View rootView = getWindow().getDecorView().getRootView();
        AppsKitSDKAdsManager.INSTANCE.loadInterstitial(this, Utils.createAdKeyFromScreenId(currentFragment != null ? currentFragment : getScreenId()), new AdsCallback() {
            @Override
            public void onFailedToLoad() {

            }

            @Override
            public void onLoaded() {

            }

            @Override
            public void onAdShown() {

            }

            @Override
            public void onAdDismissed() {

            }

            @Override
            public void onAdFailedToShow() {

            }
        });
        rootView.postDelayed(() -> {
            if (isVisible) {
                if (!MyApplication.getInstance().isInterstitialShown() || !MyApplication.getInstance().isFragInterstitialShown())
                    requestInterstitial();
                MyApplication.getInstance().setFragInterstitialShownState(true);
                MyApplication.getInstance().setInterstitialShownState(true);
            }
        }, 500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isVisible = false;
    }

    @Override
    public Pair<Integer, String> setScreenNameAndId() {
        if (getScreenId() != null)
            return new Pair<>(getScreenId().getID(), getScreenId().toString());
        return null;
    }

    @Override
    protected void onDestroy() {
        MyApplication.getInstance().setInterstitialShownState(false);
        super.onDestroy();
    }

    @Override
    public void onFeaturePromotionClicked(String targetScreen) {
        ScreenIDs targetScreenId = ScreenIDs.getPair(targetScreen);
        // do nothing
    }

    public String getAdsKey() {
        return Utils.createAdKeyFromScreenId(getScreenId());
    }

    protected void requestInterstitial() {
        AppsKitSDKAdsManager.INSTANCE.showInterstitial(this, Utils.createAdKeyFromScreenId(currentFragment != null ? currentFragment : getScreenId()), null, Utils.isInterstitialLoadingEnabled());
    }

    protected abstract @NonNull ScreenIDs getScreenId();

    protected abstract boolean isPortrait();
}
