package com.pentabit.cvmaker.resumebuilder.views.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.pentabit.cvmaker.resumebuilder.databinding.ActivityPreviewPdfResumeBinding;
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs;
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils;

import java.io.File;

public class PreviewPdfResumeActivity extends AdBaseActivity {

    ActivityPreviewPdfResumeBinding binding;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewPdfResumeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        file = new File(getIntent().getStringExtra("path"));
        binding.pdfViewer.fromUri(Uri.fromFile(file));
        handleClicks();
    }

    @Override
    protected void onInternetConnectivityChange(Boolean isInternetAvailable) {
        AppsKitSDKUtils.setVisibility(!isInternetAvailable, binding.myCoordinatorLayout);
    }

    @NonNull
    @Override
    protected ScreenIDs getScreenId() {
        return null;
    }

    @Override
    protected boolean isPortrait() {
        return true;
    }

    private void handleClicks() {

    }
}