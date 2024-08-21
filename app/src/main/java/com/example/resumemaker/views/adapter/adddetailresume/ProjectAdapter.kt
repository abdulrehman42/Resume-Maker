package com.example.resumemaker.views.adapter.adddetailresume

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.resumemaker.databinding.EducationitemsBinding
import com.example.resumemaker.models.api.ProfileModelAddDetailResponse

class ProjectAdapter : ListAdapter<ProfileModelAddDetailResponse.UserProject, ProjectAdapter.ViewHolder>(TemplateDiffCallback) {
    inner class ViewHolder(private val binding: EducationitemsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun setData(model: ProfileModelAddDetailResponse.UserProject) {
            binding.degreeName.text=model.description
            binding.universityname.text=model.title
            binding.editEdu.setOnClickListener {
                editItemClickCallback?.invoke(model)
            }
            binding.deleteEdu.setOnClickListener {
                deleteitemClickCallback?.invoke(model)
            }
        }
    }

    var editItemClickCallback: ((ProfileModelAddDetailResponse.UserProject) -> Unit)? = null
    var deleteitemClickCallback: ((ProfileModelAddDetailResponse.UserProject) -> Unit)? = null


    fun setOnEditItemClickCallback(callback: (ProfileModelAddDetailResponse.UserProject) -> Unit) {
        this.editItemClickCallback = callback
    }

    fun setOnItemDeleteClickCallback(callback: (ProfileModelAddDetailResponse.UserProject) -> Unit) {
        this.deleteitemClickCallback = callback
    }

    object TemplateDiffCallback : DiffUtil.ItemCallback<ProfileModelAddDetailResponse.UserProject>() {
        override fun areItemsTheSame(oldItem: ProfileModelAddDetailResponse.UserProject, newItem: ProfileModelAddDetailResponse.UserProject): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProfileModelAddDetailResponse.UserProject, newItem: ProfileModelAddDetailResponse.UserProject): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = EducationitemsBinding.inflate(
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