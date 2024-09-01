package com.pentabit.cvmaker.resumebuilder.utils;

import android.os.SystemClock;
import android.view.View;

import androidx.annotation.NonNull;

import com.pentabit.cvmaker.resumebuilder.views.activities.AdBaseActivity;
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKEventType;

public abstract class DebounceClickListener implements View.OnClickListener {

    private static final int DEFAULT_DEBOUNCE_INTERVAL = 2; // 2 seconds
    private long debounceInterval;
    private long lastClickTime = 0;

    private final String buttonName;


    public DebounceClickListener(@NonNull String btnName) {
        this(DEFAULT_DEBOUNCE_INTERVAL, btnName);
    }

    public DebounceClickListener(int debounceIntervalInSeconds, @NonNull String btnName) {
        this.debounceInterval = debounceIntervalInSeconds * 1000L;
        this.buttonName = btnName;
    }

    @Override
    public final void onClick(View v) {
        long currentTime = SystemClock.elapsedRealtime();
        if (currentTime - lastClickTime < debounceInterval) {
            // Ignore the click as it happened within the debounce interval
            return;
        }
        if (v.getContext() instanceof AdBaseActivity adbaseActivity) {
            adbaseActivity.sendAKSEvent(AppsKitSDKEventType.BUTTON, buttonName);
        }
        lastClickTime = currentTime;
        onDebouncedClick(v);
    }

    public abstract void onDebouncedClick(View v);
}
