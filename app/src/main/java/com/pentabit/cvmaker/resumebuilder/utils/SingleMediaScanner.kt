package com.pentabit.cvmaker.resumebuilder.utils

import android.content.Context
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.MediaScannerConnectionClient
import android.net.Uri
import java.io.File


class SingleMediaScanner(context: Context?, private val mFile: File, private val mimeType: String) :
    MediaScannerConnectionClient {
    private val mMs = MediaScannerConnection(context, this)

    init {
        mMs.connect()
    }

    override fun onMediaScannerConnected() {
        mMs.scanFile(mFile.absolutePath, mimeType)
    }

    override fun onScanCompleted(path: String, uri: Uri) {
        mMs.disconnect()
    }
}