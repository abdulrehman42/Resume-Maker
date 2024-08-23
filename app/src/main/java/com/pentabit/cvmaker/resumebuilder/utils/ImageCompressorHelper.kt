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
        if (file == null) return
        GlobalScope.launch(Dispatchers.IO) {
            val requestBody: RequestBody = RequestBody.create(
                "image/png".toMediaTypeOrNull(),
                runCompressor(file, ResumeMakerApplication.instance)
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