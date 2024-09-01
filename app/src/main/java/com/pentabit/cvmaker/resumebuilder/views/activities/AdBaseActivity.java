package com.pentabit.cvmaker.resumebuilder.views.activities;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication;
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs;
import com.pentabit.cvmaker.resumebuilder.utils.Utils;
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager;
import com.pentabit.pentabitessentials.ads_manager.ads_callback.AdsCallback;
import com.pentabit.pentabitessentials.views.AppsKitSDKBaseActivity;

public abstract class AdBaseActivity extends AppsKitSDKBaseActivity {

    private boolean isVisible = false;
    protected ScreenIDs currentFragmentId = null;
    protected String placeHolder;
    protected ScreenIDs screen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placeHolder = getAdsKey();
        screen = getScreenId();
        ResumeMakerApplication.Companion.getInstance().setInterstitialShownState(false);
        if (isPortrait())
            restrictPortraitOnly();
    }

    public void setCurrentFragmentId(ScreenIDs currentFragment) {
        this.currentFragmentId = currentFragment;
    }

    public void askAdOnFragment(ScreenIDs currentFragment) {
        setCurrentFragmentId(currentFragment);
        requestInterstitial();
        ResumeMakerApplication.Companion.getInstance().setFragInterstitialShownState(true);
        ResumeMakerApplication.Companion.getInstance().setInterstitialShownState(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        isVisible = true;
        View rootView = getWindow().getDecorView().getRootView();
        AppsKitSDKAdsManager.INSTANCE.loadInterstitial(this, Utils.createAdKeyFromScreenId(currentFragmentId != null ? currentFragmentId : getScreenId()), new AdsCallback() {
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
                if (!ResumeMakerApplication.Companion.getInstance().isInterstitialShown() || !ResumeMakerApplication.Companion.getInstance().isFragInterstitialShown())
                    requestInterstitial();
                ResumeMakerApplication.Companion.getInstance().setFragInterstitialShownState(true);
                ResumeMakerApplication.Companion.getInstance().setInterstitialShownState(true);
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
        ResumeMakerApplication.Companion.getInstance().setInterstitialShownState(false);
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
        AppsKitSDKAdsManager.INSTANCE.showInterstitial(this, Utils.createAdKeyFromScreenId(currentFragmentId != null ? currentFragmentId : getScreenId()), null, Utils.isInterstitialLoadingEnabled());
    }

    protected abstract @NonNull ScreenIDs getScreenId();

    protected abstract boolean isPortrait();
}
