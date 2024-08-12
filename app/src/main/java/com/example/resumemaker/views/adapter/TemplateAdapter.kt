package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resumemaker.databinding.TemplatelayoutitemsBinding
import com.example.resumemaker.models.TemplateModel

class TemplateAdapter(
    val context: Context,
    val list: List<TemplateModel>,
    val onclick: (TemplateModel) -> Unit
) : RecyclerView.Adapter<TemplateAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: TemplatelayoutitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: TemplateModel) {
            Glide.with(context).load(model.image).into(binding.templateimage)
            binding.templateimage.setOnClickListener {
                onclick(model)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TemplatelayoutitemsBinding.inflate(
            LayoutInflater.from(context),
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