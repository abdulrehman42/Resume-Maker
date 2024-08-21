package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.SkillitemsBinding

class LanguageAdapter : ListAdapter<String, LanguageAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: SkillitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: String) {
            binding.skillname.text=model
            binding.editSkill.setOnClickListener {
                editItemClickCallback?.invoke(model)
            }
            binding.deleteSkill.setOnClickListener {
                deleteitemClickCallback?.invoke(model)
            }
        }
    }

    var editItemClickCallback: ((String) -> Unit)? = null
    var deleteitemClickCallback: ((String) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (String) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (String) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SkillitemsBinding.inflate(
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