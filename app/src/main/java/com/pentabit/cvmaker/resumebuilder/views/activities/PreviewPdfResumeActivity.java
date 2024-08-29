package com.pentabit.cvmaker.resumebuilder.views.activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.pentabit.cvmaker.resumebuilder.databinding.ActivityPreviewPdfResumeBinding;

import java.io.File;

public class PreviewPdfResumeActivity extends AppCompatActivity {

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

    private void handleClicks() {

    }
}