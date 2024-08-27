package com.pentabit.cvmaker.resumebuilder.utils

import android.content.Context
import com.pentabit.cvmaker.resumebuilder.callbacks.OnImageCompressed
import com.pentabit.cvmaker.resumebuilder.viewmodels.AddDetailResumeVM
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogType
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.Compression
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class ImageCompressorHelper {
    fun compressInBackground(file: File?, callback: OnImageCompressed) {
        if (file.toString().isNullOrBlank()) {
            callback.onCompressFailed()
            return
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val requestBody: RequestBody = RequestBody.create(
                    "image/png".toMediaTypeOrNull(),
                    runCompressor(file!!, ResumeMakerApplication.instance)
                )
                AppsKitSDKLogManager.getInstance().log(
                    AppsKitSDKLogType.WARNING,
                    "Compressed File Size in MBs : " + file.length()
                )
                callback.pmImageCompressed(
                    MultipartBody.Part.createFormData(
                        "image",
                        file.name,
                        requestBody
                    )
                )
            }


            GlobalScope.launch(Dispatchers.IO) {
                try {
                    val compressedFile = runCompressor(file!!, ResumeMakerApplication.instance)

                    // Ensure the compressed file is not null or empty
                    if (compressedFile == null || compressedFile.isEmpty()) {
                        AppsKitSDKLogManager.getInstance().log(
                            AppsKitSDKLogType.ERROR,
                            "Compression failed: Compressed file is null or empty."
                        )
                        return@launch
                    }

                    val requestBody: RequestBody = RequestBody.create(
                        "image/png".toMediaTypeOrNull(),
                        compressedFile
                    )
                    AppsKitSDKLogManager.getInstance().log(
                        AppsKitSDKLogType.WARNING,
                        "Compressed File Size in MBs : ${file.length() / (1024 * 1024)} MB"
                    )
                    callback.pmImageCompressed(
                        MultipartBody.Part.createFormData(
                            "image",
                            file.name,
                            requestBody
                        )
                    )
                } catch (e: Exception) {
                    AppsKitSDKLogManager.getInstance().log(
                        AppsKitSDKLogType.ERROR,
                        "Error during compression: ${e.message}"
                    )
                }
            }
        }
    }

    fun File?.isEmpty(): Boolean {
        return this == null || !this.exists() || this.length() == 0L
    }

    private suspend fun runCompressor(imageFile: File, context: Context): File {
        try {
            Compression().apply { default() }
            return Compressor.compress(
                context,
                imageFile
            )
        } catch (e: Exception) {
            e.printStackTrace()
            throw e
        }
    }
}