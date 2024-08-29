package com.pentabit.cvmaker.resumebuilder.views.activities;


import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.pentabit.cvmaker.resumebuilder.databinding.ActivityPreviewImageResumeBinding;
import com.pentabit.cvmaker.resumebuilder.utils.Helper;

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
        binding.delete.setOnClickListener(v -> {
            if (file.exists()) {
                if (file.delete()) {
                    Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(this, "Failed to delete the file", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
            }
        });

        binding.share.setOnClickListener(v -> {
            Helper.INSTANCE.share_Image(this,file.getAbsolutePath());
        });
        handleClicks();
    }

    private void handleClicks() {

    }


}