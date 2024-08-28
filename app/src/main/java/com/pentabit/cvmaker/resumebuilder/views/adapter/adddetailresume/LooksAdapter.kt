package com.pentabit.cvmaker.resumebuilder.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pentabit.cvmaker.resumebuilder.databinding.LooksitemBinding
import com.pentabit.cvmaker.resumebuilder.models.api.LookUpResponse

class LooksAdapter :
    ListAdapter<LookUpResponse, LooksAdapter.ViewHolder>(TemplateDiffCallback) {

    inner class ViewHolder(private val binding: LooksitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: LookUpResponse) {
            // Bind data to views
            binding.loock.text = model.text
            itemClickCallback?.let { callback ->
                binding.root.setOnClickListener { callback(model) }
            }
        }
    }

    private var itemClickCallback: ((LookUpResponse) -> Unit)? = null

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
        val model = getItem(position)
        holder.bind(model)
    }

}