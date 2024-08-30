package com.pentabit.cvmaker.resumebuilder.views.adapter.download

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.databinding.DownloaditemBinding
import com.pentabit.cvmaker.resumebuilder.databinding.TemplatelayoutitemsBinding
import com.pentabit.cvmaker.resumebuilder.utils.ResumeMakerApplication
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding
import java.io.File
import java.io.IOException

class DownloadImgAdapter(val openPdf: (File) -> Unit) :
    ListAdapter<File, AppsKitSDKRecyclerBaseViewBinding>(FileDiffCallback) {
        var isPdf=false
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsKitSDKRecyclerBaseViewBinding {
        return AppsKitSDKRecyclerBaseViewBinding(
            DownloaditemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppsKitSDKRecyclerBaseViewBinding, position: Int) {
        val binding1 = holder.binding as DownloaditemBinding
        val currenytItem = getItem(position)
        if (isPdf(currenytItem.path))
        {
            Glide.with(ResumeMakerApplication.instance).load(getPdfThumbnail(currenytItem,100,100))
                .into(binding1.templateimage)
        }else{
            Glide.with(ResumeMakerApplication.instance).load(currenytItem)
                .into(binding1.templateimage)
        }


        binding1.templateimage.setOnClickListener {
            openPdf(currenytItem)
        }
    }

    fun isPdf(filePath: String): Boolean {
        return filePath.endsWith(".pdf", ignoreCase = true)
    }

    fun openPdfFile(context: Context, pdfFile: File) {
        try {
            val pdfUri: Uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pdfFile
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(pdfUri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

            val intentChooser = Intent.createChooser(intent, "Open PDF with")
            context.startActivity(intentChooser)
        } catch (e: Exception) {
            e.printStackTrace()
            AppsKitSDKUtils.makeToast("${e.message}")
        }
    }


    object FileDiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

        override fun areContentsTheSame(oldItem: File, newItem: File): Boolean {
            return oldItem.toString() == newItem.toString()
        }
    }

    fun getPdfThumbnail(pdfFile: File, thumbnailWidth: Int, thumbnailHeight: Int): Bitmap? {
        var parcelFileDescriptor: ParcelFileDescriptor? = null
        var pdfRenderer: PdfRenderer? = null
        var bitmap: Bitmap? = null
        try {
            // Open the PDF file
            parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            // Initialize PdfRenderer
            pdfRenderer = PdfRenderer(parcelFileDescriptor)
            // Open the first page
            val page = pdfRenderer.openPage(0)
            // Create a bitmap with the specified width and height
            bitmap = Bitmap.createBitmap(thumbnailWidth, thumbnailHeight, Bitmap.Config.ARGB_8888)
            // Render the page onto the bitmap
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            // Close the page
            page.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // Close PdfRenderer and ParcelFileDescriptor
            pdfRenderer?.close()
            parcelFileDescriptor?.close()
        }
        return bitmap
    }

}