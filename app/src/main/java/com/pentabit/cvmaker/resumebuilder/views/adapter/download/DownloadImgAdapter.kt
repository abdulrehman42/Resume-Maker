package com.pentabit.cvmaker.resumebuilder.views.adapter.download

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.databinding.TemplatelayoutitemsBinding
import com.pentabit.cvmaker.resumebuilder.utils.ResumeMakerApplication
import com.pentabit.pentabitessentials.utils.AppsKitSDKUtils
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding
import java.io.File

class DownloadImgAdapter(val openPdf: (File) -> Unit) :
    ListAdapter<File, AppsKitSDKRecyclerBaseViewBinding>(FileDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppsKitSDKRecyclerBaseViewBinding {
        return AppsKitSDKRecyclerBaseViewBinding(
            TemplatelayoutitemsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AppsKitSDKRecyclerBaseViewBinding, position: Int) {
        val binding1 = holder.binding as TemplatelayoutitemsBinding
        binding1.eyeIconId.visibility = View.GONE
        val currenytItem = getItem(position)
        Glide.with(ResumeMakerApplication.instance).load(currenytItem)
            .into(binding1.templateimage)
        binding1.templateimage.setOnClickListener {
            openPdf(currenytItem)
//            openPdfFile(binding1.root.context, currenytItem)
        }
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


}