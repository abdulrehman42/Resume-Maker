package com.pentabit.cvmaker.resumebuilder.views.adapter

import android.content.Context
import android.content.ContextWrapper
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WebViewPrintAdapter(
    private val htmlContent: String,
    private val directory: String, // Base directory for saving the PDF
    private val appDirectorySubName: String,
    val context: Context// Subdirectory within the base directory
) : PrintDocumentAdapter() {

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        callback?.onLayoutFinished(PrintDocumentInfo.Builder("document.pdf").build(), true)
    }

    override fun onWrite(
        pages: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        callback: WriteResultCallback?
    ) {
        // Define the target directory and file path
        val cw = ContextWrapper(context)

        // Get the base directory
        try {
            // Ensure the target directory exists
            val directory: File = cw.getDir(directory, Context.MODE_PRIVATE)

            // Navigate to the subdirectory (header/appDirectorySubName/Pdf)
            val targetDirectory = File(directory, "$appDirectorySubName/Pdf")

            if (!targetDirectory.exists()) {
                val created = targetDirectory.mkdirs()
                if (!created) {
                    throw IOException("Failed to create directory: ${targetDirectory.absolutePath}")
                }
            }

            // Use the file descriptor directly if available
            val fileDescriptor = parcelFileDescriptor?.fileDescriptor
            if (fileDescriptor != null) {
                FileOutputStream(fileDescriptor).use { fileOutputStream ->
                    val pdfDocument = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(612, 792, 1).create()
                    val page = pdfDocument.startPage(pageInfo)


                    pdfDocument.finishPage(page)
                    pdfDocument.writeTo(fileOutputStream)
                    pdfDocument.close()

                    callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                }
            } else {
                throw IOException("File descriptor is null")
            }
        } catch (e: IOException) {
            e.printStackTrace()
            callback?.onWriteFailed(e.toString())
        }
    }
}