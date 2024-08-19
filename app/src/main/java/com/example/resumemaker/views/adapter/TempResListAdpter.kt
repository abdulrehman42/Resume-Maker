package com.example.resumemaker.views.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.resumemaker.databinding.TemplatelayoutitemsBinding
import com.example.resumemaker.models.api.TemplateModel
import com.example.resumemaker.utils.Constants

class TempResListAdpter: ListAdapter<TemplateModel,TempResListAdpter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: TemplatelayoutitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: TemplateModel) {
            Glide.with(binding.templateimage.context).load(Constants.BASE_MEDIA_URL+model.path).into(binding.templateimage)
            binding.eyeIconId.setOnClickListener {
                eyeItemClickCallback?.invoke(model)
            }
            binding.templateimage.setOnClickListener{
                itemClickCallback?.invoke(model)
            }
        }
    }

     var eyeItemClickCallback: (( TemplateModel) -> Unit)? = null
     var itemClickCallback: ((TemplateModel) -> Unit)? = null


    fun setOnEyeItemClickCallback(callback: (TemplateModel) -> Unit) {
        this.eyeItemClickCallback=callback
    }
    fun setOnItemClickCallback(callback: (TemplateModel) -> Unit) {
        this.itemClickCallback=callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<TemplateModel>() {
        override fun areItemsTheSame(oldItem: TemplateModel, newItem: TemplateModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TemplateModel, newItem: TemplateModel): Boolean {
            return oldItem == newItem
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(getItem(position))
    }
}