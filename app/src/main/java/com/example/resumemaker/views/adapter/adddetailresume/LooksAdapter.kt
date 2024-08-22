package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.LooksitemBinding
import com.example.resumemaker.models.api.LookUpResponse
import com.example.resumemaker.models.api.TemplateModel

class LooksAdapter :
    ListAdapter<LookUpResponse, LooksAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: LooksitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: LookUpResponse) {
            binding.loock.text = model.text
            itemClickCallback?.invoke(model)
        }
    }

    var itemClickCallback: ((LookUpResponse) -> Unit)? = null

    fun setOnItemClickCallback(callback: (LookUpResponse) -> Unit) {
        this.itemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<LookUpResponse>() {
        override fun areItemsTheSame(oldItem: LookUpResponse, newItem: LookUpResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LookUpResponse, newItem: LookUpResponse): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = LooksitemBinding.inflate(
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