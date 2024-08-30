package com.pentabit.cvmaker.resumebuilder.utils;

import static com.pentabit.cvmaker.resumebuilder.utils.Constants.SHOW_LOADING_BEFORE_INTERSTITIAL;

import com.pentabit.pentabitessentials.pref_manager.AppsKitSDKPreferencesManager;

import java.text.MessageFormat;

public class Utils {
    private static final boolean isInterstitialLoadingEnabled = AppsKitSDKPreferencesManager.getInstance().getBooleanPreferences(SHOW_LOADING_BEFORE_INTERSTITIAL, true);

    public static boolean isInterstitialLoadingEnabled() {
        return isInterstitialLoadingEnabled;
    }
    public static String createAdKeyFromScreenId(ScreenIDs screenId) {
        return MessageFormat.format("{0}_{1}", screenId.getID(), screenId.toString());
    }
}
