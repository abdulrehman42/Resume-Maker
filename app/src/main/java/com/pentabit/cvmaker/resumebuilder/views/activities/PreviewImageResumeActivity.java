package com.pentabit.cvmaker.resumebuilder.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityPreviewImageResumeBinding;

import java.io.File;

public class PreviewImageResumeActivity extends AppCompatActivity {

    ActivityPreviewImageResumeBinding binding;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPreviewImageResumeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        file = new File(getIntent().getStringExtra("path"));
        Glide.with(this).load(file).into(binding.image);
        handleClicks();
    }

    private void handleClicks() {

    }
}