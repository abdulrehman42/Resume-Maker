package com.pentabit.cvmaker.resumebuilder.utils;

import static com.pentabit.cvmaker.resumebuilder.utils.Constants.SHOW_LOADING_BEFORE_INTERSTITIAL;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication;
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

    public enum InternetConnectionType {
        WIFI,
        MOBILE_DATA,
        NO_INTERNET
    }


    public static InternetConnectionType isInternetThroughWiFi() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                ResumeMakerApplication.Companion.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        if (activeNetwork != null && activeNetwork.isConnected()) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return InternetConnectionType.WIFI;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                return InternetConnectionType.MOBILE_DATA;
            }
        }
        return InternetConnectionType.NO_INTERNET;
    }

}
