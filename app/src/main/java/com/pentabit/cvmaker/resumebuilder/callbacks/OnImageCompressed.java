package com.pentabit.cvmaker.resumebuilder.callbacks;


import okhttp3.MultipartBody;

public interface OnImageCompressed {
    void pmImageCompressed(MultipartBody.Part multipartBody);
}
