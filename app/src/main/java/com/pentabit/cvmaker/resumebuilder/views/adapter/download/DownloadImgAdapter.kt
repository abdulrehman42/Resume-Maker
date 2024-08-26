package com.pentabit.cvmaker.resumebuilder.views.adapter.download

import android.app.Activity
import android.content.Intent
import android.net.Uri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.databinding.TemplatelayoutitemsBinding
import com.pentabit.cvmaker.resumebuilder.utils.ResumeMakerApplication
import com.pentabit.pentabitessentials.views.AppsKitSDKRecyclerBaseViewBinding
import java.io.File

class DownloadImgAdapter(val activity:Activity,val openPdf:(File)->Unit) : ListAdapter<File, AppsKitSDKRecyclerBaseViewBinding>(FileDiffCallback) {

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