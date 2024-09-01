package com.pentabit.cvmaker.resumebuilder.utils;

import android.content.Context;

import com.pentabit.pentabitessentials.firebase.AppsKitSDK;
import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FreeTaskManager {

    private static FreeTaskManager instance = null;
    private static final String IS_PRO_PURCHASE = "IS_PRO_PURCHASE";
    private static final String THROUGH_SUBSCRIPTION = "THROUGH_SUBSCRIPTION";
    private static final String STORED_DATE = "STORED_DATE";
    String date = new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date());

    private FreeTaskManager() {
    }

    public static FreeTaskManager getInstance(Context activity) {
        return getInstance();
    }

    public static FreeTaskManager getInstance() {
        if (instance == null) instance = new FreeTaskManager();
        return instance;
    }

    public void removeAdPurchased(boolean flag) {
        AppsKitSDK.getInstance().setRemoveAdsStatus(flag);
    }

    public void resetAllCounts() {

    }

    public void setDate() {
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(STORED_DATE, date);
    }

    public boolean needToShuffle() {
        return !date.equals(getDate());
    }

    private String getDate() {
        return AppsKitSDKPreferencesManager.getInstance().getStringPreferences(STORED_DATE, "");
    }

    public void proPurchased() {
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(IS_PRO_PURCHASE, true);
        removeAdPurchased(true);
    }

    public void proPurchased(boolean flag) {
        if (flag)
            AppsKitSDKPreferencesManager.getInstance().addInPreferences(THROUGH_SUBSCRIPTION, false);
        proPurchased();
    }

    public void unSubscribeProSub() {
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(THROUGH_SUBSCRIPTION, false);
        AppsKitSDKPreferencesManager.getInstance().addInPreferences(IS_PRO_PURCHASE, false);
        removeAdPurchased(false);
        AppsKitSDK.getInstance().setRemoveAdsStatus(false);
    }

    public boolean isThroughSubscriptionPurchased() {
        return AppsKitSDKPreferencesManager.getInstance().getBooleanPreferences(THROUGH_SUBSCRIPTION, false);
    }

    public boolean isProPurchased() {
        return AppsKitSDKPreferencesManager.getInstance().getBooleanPreferences(IS_PRO_PURCHASE, false);
    }

    public boolean isRemovedAdPurchased() {
        return AppsKitSDK.getInstance().getRemoveAdsStatus();
    }
}
