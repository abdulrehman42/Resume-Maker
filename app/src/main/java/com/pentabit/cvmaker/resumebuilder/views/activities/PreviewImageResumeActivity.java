package com.pentabit.cvmaker.resumebuilder.views.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityPreviewImageResumeBinding;
import com.pentabit.cvmaker.resumebuilder.utils.ImageFileUtils;
import com.pentabit.cvmaker.resumebuilder.utils.ScreenIDs;
import com.pentabit.cvmaker.resumebuilder.utils.Utils;
import com.pentabit.pentabitessentials.ads_manager.AppsKitSDKAdsManager;
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils;

import java.io.File;

public class PreviewImageResumeActivity extends AdBaseActivity {

    ActivityPreviewImageResumeBinding binding;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewImageResumeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbarcontainer.textView.setText("Preview");

        file = new File(getIntent().getStringExtra("path"));
        Glide.with(this).load(file).into(binding.image);
        handleClicks();
        handleAds();
    }

    private void handleAds() {
        AppsKitSDKAdsManager.INSTANCE.showBanner(
                this,
                binding.banner,
                Utils.createAdKeyFromScreenId(getScreenId())
        );
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
        binding.delete.setOnClickListener(v -> {
            ImageFileUtils.getInstance().deleteDirectory(file);
            Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        });

        binding.share.setOnClickListener(v -> {
            if (file.exists()) {
                Uri fileUri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/jpg"); // Adjust the MIME type according to your file type
                intent.putExtra(Intent.EXTRA_STREAM, fileUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivity(Intent.createChooser(intent, "Share file using"));
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }

        });
        binding.toolbarcontainer.backbtn.setOnClickListener(v -> finish());
    }


}