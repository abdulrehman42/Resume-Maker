package com.pentabit.cvmaker.resumebuilder.utils

import android.content.Context
import com.pentabit.cvmaker.resumebuilder.application.ResumeMakerApplication
import com.pentabit.cvmaker.resumebuilder.callbacks.OnImageCompressed
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogManager
import com.pentabit.pentabitessentials.logs_manager.AppsKitSDKLogType
import com.pentabit.pentabitessentials.utils.FILE_NAME_PATTERN
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ImageCompressorHelper {

    fun compressInBackground(file: File?, callback: OnImageCompressed) {
        if (file.toString().isNullOrBlank()) {
            callback.onCompressFailed()
            return
        } else {
            var compress: File?
            GlobalScope.launch(Dispatchers.IO) {
                compress =
                    ImageFileUtils.getInstance().copyFileUsingStreams(
                        ResumeMakerApplication.instance,
                        SimpleDateFormat(FILE_NAME_PATTERN, Locale.US).format(
                            Calendar.getInstance().time
                        ), true, runCompressor(file!!, ResumeMakerApplication.instance)
                    )
                val requestBody: RequestBody = RequestBody.create(
                    "image/png".toMediaTypeOrNull(),
                    runCompressor(compress!!, ResumeMakerApplication.instance)
                )
                AppsKitSDKLogManager.getInstance().log(
                    AppsKitSDKLogType.WARNING,
                    "Compressed File Size in MBs : " + compress!!.length()
                )
                callback.pmImageCompressed(
                    MultipartBody.Part.createFormData(
                        "image",
                        compress!!.name,
                        requestBody
                    )
                )
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