package com.pentabit.cvmaker.resumebuilder.views.adapter.download

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pentabit.cvmaker.resumebuilder.databinding.TemplatelayoutitemsBinding

class DownloadImgAdapter(
    val list: MutableList<String>,
) : RecyclerView.Adapter<DownloadImgAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: TemplatelayoutitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: String) {
            Glide.with(binding.templateimage.context).load(model).into(binding.templateimage)
            binding.eyeIconId.isGone=true
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TemplatelayoutitemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(list[position])
    }


}